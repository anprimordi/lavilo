package com.makaryostudio.lavilo.feature.management.check.report.detail

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.makaryostudio.lavilo.feature.main.ui.order.detail.PdfDocumentAdapter
import com.makaryostudio.lavilo.utils.Common
import kotlinx.android.synthetic.main.fragment_check_report_detail.*
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class CheckReportDetailFragment : Fragment() {

    val fileName: String = "test_pdf.pdf"

    private lateinit var listOrderDetail: ArrayList<OrderDetail>
    private lateinit var adapter: CheckReportDetailAdapter
    private lateinit var dbReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_report_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: CheckReportDetailFragmentArgs by navArgs()
        val order = args.order

        val locale = Locale("in", "ID")

        val formatRupiah = NumberFormat.getNumberInstance(locale)

        val billRupiah = formatRupiah.format(order.bill!!.toDouble())

        val paymentRupiah = formatRupiah.format(order.payment!!.toDouble())

        val changeRupiah = formatRupiah.format(order.change!!.toDouble())

        listOrderDetail = ArrayList()

        dbReference = FirebaseDatabase.getInstance().reference

        text_report_detail_id.text = order.id
        text_report_detail_status.text = order.status
        text_report_detail_timestamp.text = order.timestamp
        text_report_detail_table_number.text = order.tableNumber
        text_report_detail_bill.text = billRupiah
        text_report_detail_payment.text = paymentRupiah
        text_report_detail_change.text = changeRupiah

        if (order.status == "Lunas") {
            text_report_detail_status.setTextColor(Color.rgb(165, 217, 213))
            button_report_detail_payment.isEnabled = true
        }

        dbReference.child("OrderDetail")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                    Log.d("call check report db", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    listOrderDetail.clear()

                    for (postSnapshot in p0.children) {
                        val orderDetail = postSnapshot.getValue(OrderDetail::class.java)!!

                        if (order.id == orderDetail.id) {
                            listOrderDetail.add(orderDetail)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            })

        rv_report_detail.layoutManager = LinearLayoutManager(requireContext())
        adapter = CheckReportDetailAdapter(requireContext(), listOrderDetail)
        rv_report_detail.adapter = adapter

        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    button_report_detail_payment.setOnClickListener {
                        //                        updateOrderStatus(receivedOrderId)
                        createPdfFile(Common.getAppPath(requireContext()) + fileName, args.order)
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

    private fun createPdfFile(path: String, order: Order) {

        val locale = Locale("in", "ID")

        val payment = order.payment!!.toInt()
        val bill = order.bill!!.toInt()
        val change = order.change!!.toInt()

        val formatRupiah = NumberFormat.getNumberInstance(locale)


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

//            addNewItem(document, "Account Name", Element.ALIGN_LEFT, headingStyle)
//            addNewItem(document, "John Doe", Element.ALIGN_LEFT, valueStyle)

            addLineSeparator(document)

            addLineSpace(document)

            addNewItem(document, "Rincian Pesanan", Element.ALIGN_LEFT, headingStyle)

            addNewItemWithFourColumn(
                document,
                valueStyle
            )

            for (i in 0 until listOrderDetail.size) {
                val orderDetail = listOrderDetail[i]

                val totalPrice = orderDetail.totalPrice.toInt()

                val quantity = orderDetail.quantity.toInt()

                val itemPrice = totalPrice / quantity

                val priceRupiah = formatRupiah.format(orderDetail.totalPrice.toDouble())

                val itemRupiah = formatRupiah.format(itemPrice.toDouble())

                addNewItemWithFiveColumns(
                    document,
                    orderDetail.name,
                    itemRupiah,
                    orderDetail.quantity,
                    priceRupiah,
                    itemStyle,
                    itemStyle,
                    itemStyle,
                    itemStyle,
                    itemStyle
                )

                addLineSpace(document)
            }

//            total
            addLineSpace(document)
            addLineSpace(document)

            addLineSeparator(document)
            addSpace(document)
            addNewItemWithThreeColumns(
                document,
                "Total Harga",
                billRupiah,
                headingStyle,
                itemStyle
            )

            addNewItemWithThreeColumns(
                document,
                "Dibayar",
                paymentRupiah,
                headingStyle,
                itemStyle
            )

            addNewItemWithThreeColumns(
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

//            Toast.makeText(requireContext(), "Berhasil", Toast.LENGTH_SHORT).show()

            printPdf()
        } catch (e: Exception) {
            Log.e("document exception", e.message!!)
        }
    }

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

    @Throws(DocumentException::class)
    private fun addNewItemWithThreeColumns(
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

    @Throws(DocumentException::class)
    private fun addNewItemWithFourColumn(
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

    @Throws(DocumentException::class)
    private fun addNewItemWithFiveColumns(
        document: Document,
        textFirst: String,
        textSecond: String,
        textThird: String,
        textFifth: String,
        styleFirst: Font,
        styleSecond: Font,
        styleThird: Font,
        styleFourth: Font,
        styleFifth: Font
    ) {
        val chunkTextFirst = Chunk(textFirst, styleFirst)
        val chunkTextSecond = Chunk(textSecond, styleSecond)
        val chunkTextThird = Chunk(textThird, styleThird)
        val chunkTextFourth = Chunk("Rp", styleFourth)
        val chunkTextFifth = Chunk(textFifth, styleFifth)

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

    @Throws(DocumentException::class)
    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)

        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    @Throws(DocumentException::class)
    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    private fun addSpace(document: Document) {
        val chunk = Chunk(" ")
        val p = Paragraph(chunk)
        document.add(p)
    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, alignment: Int, textStyle: Font) {
        val chunk = Chunk(text, textStyle)
        val p = Paragraph(chunk)
        p.alignment = alignment
        document.add(p)
    }

//    private fun convertToCurrency(nominal: Int, locale: Locale) : String {
//        return NumberFormat.getNumberInstance(locale).format(nominal)
//    }
}
