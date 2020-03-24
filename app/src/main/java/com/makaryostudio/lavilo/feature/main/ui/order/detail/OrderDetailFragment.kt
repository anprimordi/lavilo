package com.makaryostudio.lavilo.feature.main.ui.order.detail

import android.Manifest
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderDetailFragment : Fragment() {

    val fileName: String = "test_pdf.pdf"

    private lateinit var listOrderDetail: ArrayList<OrderDetail>
    private lateinit var adapter: OrderDetailFragmentAdapter
    private lateinit var dbReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: OrderDetailFragmentArgs by navArgs()

        val receivedOrderId = args.order!!.id

        listOrderDetail = ArrayList()

        dbReference = FirebaseDatabase.getInstance().reference

        dbReference.child("OrderDetail").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), databaseError.message, Toast.LENGTH_SHORT).show()
                Log.d("Order detail fragment", databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOrderDetail.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val orderDetail = postSnapshot.getValue(OrderDetail::class.java)!!

                    if (receivedOrderId == orderDetail.id) {
                        listOrderDetail.add(orderDetail)
                    }

//                    listOrderDetail.add(orderDetail)

                    adapter.notifyDataSetChanged()
                }
            }
        })

        adapter = OrderDetailFragmentAdapter(requireContext(), listOrderDetail)
        rv_order_detail.adapter = adapter

        rv_order_detail.layoutManager = LinearLayoutManager(requireContext())

        text_order_detail_bill.text = args.order!!.bill

        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
//                    TODO implement print to pdf feature
                    button_order_list_payment.setOnClickListener {
                        updateOrderStatus(receivedOrderId)
                        createPdfFile(Common.getAppPath(requireContext()) + fileName, args.order!!)
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
        val args: OrderDetailFragmentArgs by navArgs()

        val receivedOrderId = args.order!!.id

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

            val colorAccent = BaseColor(0, 153, 204, 255)

//            add title to document
            val titleStyle = Font(bebasNeueFont, 36.0f, Font.NORMAL, BaseColor.BLACK)

            addNewItem(document, "Order Details", Element.ALIGN_CENTER, titleStyle)

            val headingStyle = Font(bebasNeueFont, headingFontSize, Font.NORMAL, colorAccent)
            addNewItem(document, "Order No: ", Element.ALIGN_LEFT, headingStyle)

            val valueStyle = Font(bebasNeueFont, fontSize, Font.NORMAL, BaseColor.BLACK)
            addNewItem(document, order.id!!, Element.ALIGN_LEFT, valueStyle)

            val itemStyle = Font(bebasNeueFont, smallSize, Font.NORMAL, BaseColor.BLACK)

            addLineSeparator(document)

            addNewItem(document, "Order Date", Element.ALIGN_LEFT, headingStyle)
            addNewItem(document, order.timestamp!!, Element.ALIGN_LEFT, valueStyle)

            addNewItem(document, "Account Name", Element.ALIGN_LEFT, headingStyle)
            addNewItem(document, "Eddy Lee", Element.ALIGN_LEFT, valueStyle)

            addLineSeparator(document)

            addLineSpace(document)

            addNewItem(document, "Product Details", Element.ALIGN_LEFT, headingStyle)


            for (i in 0 until listOrderDetail.size) {
                val orderDetail = listOrderDetail[i]
                addNewItemWithLeftAndRight(
                    document,
                    orderDetail.name,
                    orderDetail.quantity,
                    itemStyle,
                    itemStyle
                )
                addNewItem(document, orderDetail.totalPrice, Element.ALIGN_LEFT, itemStyle)
                addLineSpace(document)
            }

////            item 1
//            addNewItemWithLeftAndRight(document, "Pizza 25", "(0.0%)", titleStyle, valueStyle)
//            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleStyle, valueStyle)
//
//            addLineSeparator(document)
//
////            item 2
//            addNewItemWithLeftAndRight(document, "Pizza 26", "(0.0%)", titleStyle, valueStyle)
//            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleStyle, valueStyle)
//
//            addLineSeparator(document)

//            total
            addLineSpace(document)
            addLineSpace(document)

            addLineSeparator(document)
            addNewItemWithLeftAndRight(document, "Total", order.bill!!, titleStyle, valueStyle)

            document.close()

            Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()

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
    private fun addNewItemWithLeftAndRight(
        document: Document,
        textLeft: String,
        textRight: String,
        leftStyle: Font,
        rightStyle: Font
    ) {
        val chunkTextLeft = Chunk(textLeft, leftStyle)
        val chunkTextRight = Chunk(textRight, rightStyle)
        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
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

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, alignment: Int, textStyle: Font) {
        val chunk = Chunk(text, textStyle)
        val p = Paragraph(chunk)
        p.alignment = alignment
        document.add(p)
    }

    private fun updateOrderStatus(receivedOrderId: String?) {
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
                        order = Order(
                            receivedOrderId,
                            "Lunas",
                            timestamp,
                            order.bill,
                            order.tableNumber,
                            order.bill,
                            "0"
                        )

                        dbReference.child("Order").child(receivedOrderId!!).setValue(order)

                        findNavController().navigate(R.id.action_orderDetailFragment_to_navigation_order)
                    }
                }
            }
        })
    }
}
