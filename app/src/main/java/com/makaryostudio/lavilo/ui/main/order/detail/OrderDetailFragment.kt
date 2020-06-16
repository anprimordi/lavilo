package com.makaryostudio.lavilo.ui.main.order.detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Order
import com.makaryostudio.lavilo.data.model.OrderDetail
import com.makaryostudio.lavilo.utils.Common
import kotlinx.android.synthetic.main.fragment_order_detail.*
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderDetailFragment : Fragment() {

    val fileName: String = "test_pdf.pdf"
    private lateinit var editPayment: TextInputLayout

    private lateinit var listOrderDetail: ArrayList<OrderDetail>
    private lateinit var adapter: OrderDetailFragmentAdapter
    private lateinit var dbReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: OrderDetailFragmentArgs by navArgs()

        val receivedOrderId = args.order!!.id

        val tableNumber = args.order!!.tableNumber

        text_order_detail_table_number.text = "Meja $tableNumber"

        editPayment = view.findViewById(R.id.edit_order_detail_payment)

        listOrderDetail = ArrayList()

        dbReference = FirebaseDatabase.getInstance().reference

        dbReference.child("OrderDetail").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), databaseError.message, Toast.LENGTH_SHORT).show()
                Log.e("Order detail fragment", databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOrderDetail.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val orderDetail = postSnapshot.getValue(OrderDetail::class.java)!!

                    if (receivedOrderId == orderDetail.id) {
                        listOrderDetail.add(orderDetail)
                    }

                    adapter.notifyDataSetChanged()
                }
            }
        })

        adapter = OrderDetailFragmentAdapter(requireContext(), listOrderDetail)
        rv_order_detail.adapter = adapter

        rv_order_detail.layoutManager = LinearLayoutManager(requireContext())

        val bill = args.order!!.bill!!.toInt()
        val locale = Locale("in", "ID")

        val formatRupiah = NumberFormat.getCurrencyInstance(locale)

        text_order_detail_bill.text = formatRupiah.format(bill.toDouble())

        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    button_order_list_payment.setOnClickListener {
                        var go = true

                        if (editPayment.editText?.text.toString() == "") {
                            editPayment.error = "nominal pembayaran belum dimasukkan"
                            editPayment.requestFocus()
                            go = false
                        }
                        if (editPayment.editText?.text!!.isNotEmpty() && editPayment.editText?.text.toString().toInt() < bill) {
                            editPayment.error = "nominal pembayaran masih kurang"
                            editPayment.requestFocus()
                            go = false
                        }
                        if (go) {
                            val payment = editPayment.editText?.text.toString().toInt()
                            updateOrderStatus(receivedOrderId, payment, bill)
                            createPdfFile(
                                Common.getAppPath(requireContext()) + fileName,
                                args.order!!,
                                payment,
                                bill
                            )
                        }

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                }
            })
            .check()
    }

//    buat file pdf
private fun createPdfFile(path: String, order: Order, payment: Int, bill: Int) {

    val locale = Locale("in", "ID")

    val formatRupiah = NumberFormat.getNumberInstance(locale)

    val change = payment - bill

    val billRupiah = formatRupiah.format(bill.toDouble())
    val paymentRupiah = formatRupiah.format(payment.toDouble())
    val changeRupiah = formatRupiah.format(change.toDouble())

    if (File(path).exists()) File(path).delete()
    try {
        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream(path))

//            open to write
        document.open()

//            setting
        document.pageSize = PageSize.A4
        document.addCreationDate()
        document.addAuthor("Lava View Lodge")
        document.addCreator("Makaryo Studio")

//            font setting
        val headingFontSize = 20.0f
        val fontSize = 26.0f
        val smallSize = 18.0f

//            custom font
        val bebasNeueFont =
            BaseFont.createFont("assets/BebasNeueRegular.otf", "UTF-8", BaseFont.EMBEDDED)

        val colorAccent = BaseColor(247, 149, 29, 255)

//            add title to document
        val titleStyle = Font(bebasNeueFont, 36.0f, Font.NORMAL, BaseColor.BLACK)

        val restoStyle = Font(bebasNeueFont, 36.0f, Font.NORMAL, colorAccent)

        addNewItem(document, "Lava View Lodge", Element.ALIGN_CENTER, titleStyle)

        val headingStyle = Font(bebasNeueFont, headingFontSize, Font.NORMAL, colorAccent)
        addNewItem(document, "ID Pesanan", Element.ALIGN_LEFT, headingStyle)

        val valueStyle = Font(bebasNeueFont, fontSize, Font.NORMAL, BaseColor.BLACK)
        addNewItem(document, order.id!!, Element.ALIGN_LEFT, valueStyle)

        val itemStyle = Font(bebasNeueFont, smallSize, Font.NORMAL, BaseColor.BLACK)

        addLineSeparator(document)

        addNewItem(document, "Waktu Pemesanan", Element.ALIGN_LEFT, headingStyle)
        addNewItem(document, order.timestamp!!, Element.ALIGN_LEFT, valueStyle)

        addLineSeparator(document)

        addLineSpace(document)

        addNewItem(document, "Rincian Pesanan", Element.ALIGN_LEFT, headingStyle)

        addOrderItemTitleColumn(
            document,
            valueStyle
        )

        for (i in 0 until listOrderDetail.size) {
            val orderDetail = listOrderDetail[i]

            val priceRupiah = formatRupiah.format(orderDetail.totalPrice.toDouble())

            val totalPrice = orderDetail.totalPrice.toInt()

            val quantity = orderDetail.quantity.toInt()

            val itemPrice = totalPrice / quantity

            val itemRupiah = formatRupiah.format(itemPrice.toDouble())

            addOrderItemColumn(
                document,
                orderDetail.name,
                itemRupiah,
                orderDetail.quantity,
                priceRupiah,
                itemStyle
            )

            addLineSpace(document)
        }

//            total
        addLineSpace(document)
        addLineSpace(document)

        addLineSeparator(document)

        addSpace(document)
        addPaymentColumn(
            document,
            "Total Harga",
            billRupiah,
            headingStyle,
            itemStyle
        )

        addPaymentColumn(
            document,
            "Dibayar",
            paymentRupiah,
            headingStyle,
            itemStyle
        )

        addPaymentColumn(
            document,
            "Kembali",
            changeRupiah,
            headingStyle,
            itemStyle
        )

        addSpace(document)
        addSpace(document)

        addNewItem(
            document,
            "Terima kasih",
            Element.ALIGN_CENTER,
            valueStyle
        )

        addNewItem(
            document,
            "atas kunjungan anda",
            Element.ALIGN_CENTER,
            valueStyle
        )

        addNewItem(
            document,
            "resto lava",
            Element.ALIGN_CENTER,
            restoStyle
        )

        document.close()

        printPdf()
    } catch (e: Exception) {
        Log.e("document exception", e.message!!)
    }
}

    //    print pdf
    private fun printPdf() {
        val printManager = requireActivity().getSystemService(Context.PRINT_SERVICE) as PrintManager
        try {
            val printAdapter =
                PdfDocumentAdapter(requireContext(), Common.getAppPath(requireContext()) + fileName)
            printManager.print("Document", printAdapter, PrintAttributes.Builder().build())
        } catch (e: Exception) {
            Log.e("print pdf error", e.message, e.cause)
        }
    }

    //    tambah nama kolom pada detail pesanan
    @Throws(DocumentException::class)
    private fun addOrderItemTitleColumn(
        document: Document,
        style: Font
    ) {
        val chunkTextFirst = Chunk("Menu", style)
        val chunkTextSecond = Chunk("Harga", style)
        val chunkTextThird = Chunk("Porsi", style)
        val chunkTextFourth = Chunk("Jumlah", style)

        val p = Paragraph(chunkTextFirst)
        p.tabSettings = TabSettings(152f)
        p.add(Chunk.TABBING)
        p.add(chunkTextSecond)
        p.add(Chunk.TABBING)
        p.add(chunkTextThird)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextFourth)
        document.add(p)
    }

    //    tambah item pada pesanan
    @Throws(DocumentException::class)
    private fun addOrderItemColumn(
        document: Document,
        textDish: String,
        textPrice: String,
        textQuantity: String,
        textTotal: String,
        style: Font
    ) {
        val chunkTextFirst = Chunk(textDish, style)
        val chunkTextSecond = Chunk(textPrice, style)
        val chunkTextThird = Chunk(textQuantity, style)
        val chunkTextFourth = Chunk("Rp", style)
        val chunkTextFifth = Chunk(textTotal, style)

        val p = Paragraph(chunkTextFirst)
        p.tabSettings = TabSettings(152f)
        p.add(Chunk.TABBING)
        p.add(chunkTextSecond)
        p.add(Chunk.TABBING)
        p.add(chunkTextThird)
        p.add(Chunk.TABBING)
        p.add(chunkTextFourth)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextFifth)
        document.add(p)
    }

    //    tambah elemen pembayaran
    @Throws(DocumentException::class)
    private fun addPaymentColumn(
        document: Document,
        textLeft: String,
        textRight: String,
        leftStyle: Font,
        rightStyle: Font
    ) {
        val chunkTextLeft = Chunk(textLeft, leftStyle)
        val chunkTextRight = Chunk(textRight, rightStyle)
        val p = Paragraph(Chunk.TABBING)
        p.tabSettings = TabSettings(50f)
        p.add(Chunk.TABBING)
        p.add(Chunk.TABBING)
        p.add(Chunk.TABBING)
        p.add(Chunk.TABBING)
        p.add(Chunk.TABBING)
        p.add(chunkTextLeft)
        p.add(Chunk.TABBING)
        p.add(Chunk(":", rightStyle))
        p.add(Chunk.TABBING)
        p.add(Chunk("Rp", rightStyle))
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        document.add(p)
    }

    //    tambah garis
    @Throws(DocumentException::class)
    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)

        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    //    tambah spasi
    @Throws(DocumentException::class)
    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    //    tambah spasi independen
    @Throws(DocumentException::class)
    private fun addSpace(document: Document) {
        val chunk = Chunk(" ")
        val p = Paragraph(chunk)
        document.add(p)
    }

    //    tambah item baru
    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, alignment: Int, textStyle: Font) {
        val chunk = Chunk(text, textStyle)
        val p = Paragraph(chunk)
        p.alignment = alignment
        document.add(p)
    }

    //    update status pesanan
    private fun updateOrderStatus(receivedOrderId: String?, payment: Int, bill: Int) {
        dbReference.child("Order").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                Log.d("Order detail fragment", p0.message)
            }

            val calendar = Calendar.getInstance()
            var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", Locale.getDefault())

            val timestamp: String = simpleDateFormat.format(calendar.time)

            override fun onDataChange(p0: DataSnapshot) {
                for (postSnapshot in p0.children) {
                    var order = postSnapshot.getValue(Order::class.java)!!

                    if (order.id == receivedOrderId) {

                        val change = payment - bill

                        order = Order(
                            receivedOrderId,
                            "Lunas",
                            timestamp,
                            order.bill,
                            order.tableNumber,
                            payment.toString(),
                            change.toString()
                        )

                        dbReference.child("Order").child(receivedOrderId!!).setValue(order)

                        findNavController().navigate(R.id.action_orderDetailFragment_to_navigation_order)
                    }
                }
            }
        })
    }
}
