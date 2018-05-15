package com.GoMobeil.H2H.UI

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Geocoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.GoMobeil.H2H.*
import com.GoMobeil.H2H.Extensions.getBitmapFromUri
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.R.string.addCity
import com.GoMobeil.H2H.Services.CustomServices
import com.GoMobeil.H2H.Services.TransperantProgressDialog
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.my_profile.*
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * Created by Admin on 16-04-18.
 */
class MyProfile : BaseActivity() {

    override lateinit var activity: Activity
    override lateinit var context: Context

    lateinit var pd: TransperantProgressDialog

    var username :String = ""

    var lsFirstName :String = ""
    var lsLastName :String = ""
    var fname: String? = ""
    var lsEmail :String = ""
    var lsMobileno :String = ""
    var lsUsername :String = ""
    var lsGender :String = ""
    var lsProfImage :String = ""
    var lsMessage = ""
    lateinit  var etBuilding:EditText
    lateinit  var etStreet:EditText
    lateinit var cbOK:Button
    lateinit var cbCancel:Button
    lateinit var cbChangeAddress:Button
    lateinit var alertdialog:AlertDialog
    var lsCompleteAddress: String = ""



    var lsprofileimagebas64: String = ""
    var bmp: Bitmap? = null
    var RESULT_LOAD_IMAGE: Int? = 100
    var PLACE_AUTOCOMPLETE_REQUEST_CODE:Int?=1
    var length: Long? = null
    var lattitude:Double?=0.0
    var longitude:Double?=0.0

    var lsAddline1:String = ""
    var lsAddline2:String = ""
    var lsAddCity:String = ""
    var lsAddState:String = ""
    var lsAddPincode:String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile)
        setTitle("My Profile")
        // hideFooter(true)

        initLayout()

    }

    fun initLayout() {

        context = this
        activity = this

        pd = TransperantProgressDialog(context)


        /*  username = App.prefs!!.user.toString()
        etusername.setText(username)
 */
        getProfile()

        tvAddress.setOnClickListener {

            getAddresswithpopup()
        }
        cvaddress.setOnClickListener {

            getAddresswithpopup()
        }

        cbSavepersonal.setOnClickListener {


            /*  lsAddCity = etCity.text.toString().trim()
              lsAddState = etState.text.toString().trim()
              lsAddPincode = etPincode.text.toString().trim()*/

            validateData()
        }

        ivaddprofileimage.setOnClickListener {
            if (checkPermissionForReadExtertalStorage()) {
                val i = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, RESULT_LOAD_IMAGE!!)
            } else {
                requestPermissionForReadExtertalStorage()
            }


        }
        ivremoveprofileimage.setOnClickListener {
            bmp = null
            lsprofileimagebas64 = ""
            cvProfileImage.setImageResource(R.drawable.uploadprofileicon)
        }
        cvProfileImage.setOnClickListener {
            if (checkPermissionForReadExtertalStorage()) {
                val i = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, RESULT_LOAD_IMAGE!!)
            } else {
                requestPermissionForReadExtertalStorage()

            }

        }

    }
    fun autocomplete() = try {
        val typeFilter = AutocompleteFilter.Builder()
                .setCountry("IN")
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build()
        val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter).build(this)
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE!!);
    }catch (e: GooglePlayServicesRepairableException){

    }catch (e: GooglePlayServicesNotAvailableException){

    }


    fun genMessage(msg: String) {
        if (lsMessage.length > 0) {
            lsMessage = lsMessage + ", " + msg;
        } else {
            lsMessage = lsMessage + " " + msg;
        }
    }

    fun getProfile(){

        Fuel.post(StaticRefs.DISPLAYPROFILE, listOf(StaticRefs.CUST_ID to prefs.cust_id))
                .responseJson(){
                    request,
                    response,
                    result ->

                    result.fold({ d ->
                        parseJson(result.get().content)
                        // pd.dismiss()
                    }, { err ->
                        //  pd.dismiss()
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }
    }

    fun parseJson(data:String){

        val json = JSONObject(data)
        if(data.contains(StaticRefs.DATA)&&json.getString(StaticRefs.DATA)!=null&&!json.getString(StaticRefs.DATA).equals("")) {
            var response = JSONObject(data).getJSONObject(StaticRefs.DATA)
            if(response.length()>0) {

                lsFirstName = response.getString("cust_firstname")
                lsLastName = response.getString("cust_lastname")
                lsEmail = response.getString("cust_email")
                lsMobileno = response.getString("cust_mobileno")
                lsUsername = response.getString("cust_username")
                lsGender = response.getString("cust_gender")
                lsprofileimagebas64 = response.getString("cust_profileimage")

                if(response.getString(StaticRefs.CUSTOMERADD)!=null&&!response.getString(StaticRefs.CUSTOMERADD).equals("null")) {
                    lsAddline1 = response.getJSONObject(StaticRefs.CUSTOMERADD).getString(StaticRefs.ADD_LINE1)
                    lsAddline2 = response.getJSONObject(StaticRefs.CUSTOMERADD).getString(StaticRefs.ADD_LINE2)

                    lsCompleteAddress=lsAddline1+","+lsAddline2
                }
                else{
                    TastyToast.makeText(context,"No Data For Address ", Toast.LENGTH_SHORT,TastyToast.ERROR).show()

                }
                /*  var addCity = response.getJSONObject(StaticRefs.CUSTOMERADD).getString(StaticRefs.ADD_CITY)
        var addState = response.getJSONObject(StaticRefs.CUSTOMERADD).getString(StaticRefs.ADD_STATE)
        var addPincode = response.getJSONObject(StaticRefs.CUSTOMERADD).getString(StaticRefs.ADD_PINCODE)*/
                // var addLine1 = responseOne.getJSONObject(StaticRefs.CUSTOMERADD).getString(StaticRefs.ADD_LINE1)


                setData()


            }
            else{
                TastyToast.makeText(context,"Data is Blank ", Toast.LENGTH_SHORT,TastyToast.ERROR).show()

            }

        }
        else{
            TastyToast.makeText(context,"No Data Found ", Toast.LENGTH_SHORT,TastyToast.ERROR).show()
        }
        /*  etCity.setText(addCity)
          etState.setText(addState)
          etPincode.setText(addPincode)*/

    }

    fun EditData(){

        if (lsprofileimagebas64.length <= 0) {
            lsprofileimagebas64 = "null"
        }

        Fuel.post(StaticRefs.EDITPROFILE, listOf((StaticRefs.CUST_ID to prefs.cust_id),(StaticRefs.USERNAME to lsUsername)
                ,(StaticRefs.ADD_LINE1 to lsAddline1),(StaticRefs.ADD_LINE2 to lsAddline2), (StaticRefs.GENDER to lsGender),(StaticRefs.ADD_LATTITUDE to lattitude),
                (StaticRefs.ADD_LONGITUDE to longitude),
                (StaticRefs.CUST_PROFILEIMAGE to lsprofileimagebas64)))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson(){
                    request,
                    response,
                    result ->

                    result.fold({ d ->
                        parseEditJson(result.get().content)
                        // pd.dismiss()
                    }, { err ->
                        //  pd.dismiss()
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        Log.d("Tag","Error is:"+err.toString())
                    })


                }
    }

    fun parseEditJson(response:String){

        val json = JSONObject(response)

        if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)){
            val error = json.getString("errors")
            TastyToast.makeText(context,error.toString(),TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
        }
        else if (json.getString(StaticRefs.STATUS).equals("Success")){

            TastyToast.makeText(context,"Profile Updated Successfully",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show()
            finish()

        }

    }

    fun setData(){
        val gender: String
        val category: String
        if (lsUsername.equals(null) || lsUsername.equals("null")) {
            etusername.setText("")
        } else {
            etusername.setText(lsUsername)
        }

        if (lsMobileno.equals(null)|| lsMobileno.equals("null")) {
            tvMyProfMobile.setText("")
        } else {
            tvMyProfMobile.setText(lsMobileno)
        }



        if (lsEmail.equals(null) || lsEmail.equals("null")) {
            tvEmailId.setText("")
        } else {
            tvEmailId.setText(lsEmail)
        }
        if (lsFirstName.equals(null) || lsFirstName.equals("null")&&(lsLastName.equals(null) || lsLastName.equals("null"))) {
            tvMyProfName.setText("")
        } else {
            tvMyProfName.setText(lsFirstName+" "+lsLastName)
        }
        val lsgender = lsGender
        if (lsgender.equals("Male")) {

            gender = "Male"
            rgGender.check(rbMale.id)
        } else if (lsgender.equals("Female")) {

            gender = "Female"
            rgGender.check(rbFemale.id)
        }
        if (!lsprofileimagebas64.equals(null) && !lsprofileimagebas64.equals("null") && !lsprofileimagebas64.equals("")) {

            cvProfileImage.loadBase64Image(lsprofileimagebas64)
        } else {
//            toast("Image null")
            TastyToast.makeText(context,"Image null",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
        }
        cvaddress.visibility = View.VISIBLE
        if (lsCompleteAddress.equals(null) || lsCompleteAddress.equals("null") || lsCompleteAddress.equals("Null") || lsCompleteAddress.startsWith(",")) {
            tvAddress.setText("Click Here To Add Address")
        } else {
            tvAddress.setText(lsCompleteAddress).toString().trim()
        }

        /* if (lsAddline1.equals(null) && lsAddline1.equals("null")) {
             etAddline1.setText("")
         } else {
             etAddline1.setText(lsAddline1)
         }
         if (lsAddline2.equals(null) && lsAddline2.equals("null")) {
             etAddline2.setText("")
         } else {
             etAddline2.setText(lsAddline2)
         }*/

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {

            val selectedImage = data.data

            length = getImageSize(selectedImage)

            if (length!! <= 1000) {
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val cursor = contentResolver.query(selectedImage!!,
                        filePathColumn, null, null, null)
                cursor!!.moveToFirst()

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val picturePath = cursor.getString(columnIndex)
                cursor.close()

                try {
                    bmp = getBitmapFromUri(selectedImage)
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

                compressImage(picturePath)
                cvProfileImage.setImageBitmap(bmp)
                val imagestring = CustomServices.ConverttoBase64(bmp!!)
                FinalBase64(imagestring)

            } else {
                TastyToast.makeText(context, "Please select file upto 5MB", 30, TastyToast.ERROR).show();

            }
        }
        else if(requestCode==PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if(resultCode== RESULT_OK){
                var place= PlaceAutocomplete.getPlace(context,data)
                var address=place.address

                lattitude=place.latLng.latitude
                longitude=place.latLng.longitude

                val geocoder= Geocoder(context, Locale.ENGLISH)


                etStreet.setText(address)


            }else if(requestCode== PlaceAutocomplete.RESULT_ERROR){
                var Status= PlaceAutocomplete.getStatus(context,data)
                TastyToast.makeText(context,Status.toString(),Toast.LENGTH_SHORT,TastyToast.ERROR)

            }
        }


    }

    fun FinalBase64(encoded: String?) {
        lsprofileimagebas64 = "data:image/jpeg;base64," + encoded.toString().trim()


        // adapter.notifyItemInserted(docsModel.size - 1)
    }

    fun getImageSize(uri: Uri): Long {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val filePath = cursor.getString(columnIndex)
        cursor.close()

        val file = File(filePath)
        val ls = file.length()
        val lengthInKb = ls / 5120
        return lengthInKb
    }

    fun checkPermissionForReadExtertalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        } else {
            return false
        }
    }

    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    100)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    fun compressImage(imageUri: String): String {

        val filePath = getRealPathFromURI(imageUri)
        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        val maxHeight = 812.0f
        val maxWidth = 612.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight


        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        options.inJustDecodeBounds = false

        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: " + orientation)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90F)
                Log.d("EXIF", "Exif: " + orientation)
            } else if (orientation == 3) {
                matrix.postRotate(180F)
                Log.d("EXIF", "Exif: " + orientation)
            } else if (orientation == 8) {
                matrix.postRotate(270F)
                Log.d("EXIF", "Exif: " + orientation)
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap!!.width, scaledBitmap.height, matrix,
                    true)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        fname = getFilename()
        try {
            out = FileOutputStream(fname)

            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return fname as String
    }

    fun getFilename(): String {
        val file = File(StaticRefs.lsImageDirectory)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"

    }

    fun getRealPathFromURI(contentURI: String): String {
        val contentUri = Uri.parse(contentURI)
        val cursor = contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            return contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(index)
        }
    }

    fun validateData(){


        lsUsername = etusername.text.toString().trim()
        /* lsAddline1 = etAddline1.text.toString().trim()
         lsAddline2 = etAddline2.text.toString().trim()*/

        if (rgGender.checkedRadioButtonId == null || rgGender.checkedRadioButtonId <= 0) {

            TastyToast.makeText(context, "please select gender", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show()
        } else {
            var selectedidofgender = rgGender.checkedRadioButtonId
            var rbgender = findViewById<RadioButton>(selectedidofgender)
            lsGender = rbgender.text.toString().trim()
            if (lsGender.contains("Male")) {
                lsGender = "Male"
            } else if (lsGender.contains("Female")) {
                lsGender = "Female"
            }
        }
        var lbisProceed = true
        if (!((lsUsername != null && lsUsername.length > 0))) {
            genMessage(" Username");
            lbisProceed = false;
        }

        if (!((lsGender != null && lsGender.length > 0))) {
            genMessage(" Gender ");
            lbisProceed = false;
        }

        if (!((lsAddline1 != null && lsAddline1.length > 0))) {
            genMessage(" Flat No,Building");
            lbisProceed = false;
        }
        if (!((lsAddline2 != null && lsAddline2.length > 0))) {
            genMessage("Locality");
            lbisProceed = false;
        }

        if (!(lbisProceed) && lsMessage.length > 0) {
            if (lsMessage.length > 70) {
                TastyToast.makeText(context, "Please enter all details", TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            } else {
                TastyToast.makeText(context, "Please enter valid " + lsMessage, TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            }

        } else {
            EditData()
        }




    }

    fun getAddresswithpopup() { val cal= LayoutInflater.from(context).inflate(R.layout.address_popup,null)
        etBuilding = cal.findViewById<EditText>(R.id.etBuilding)
        etStreet = cal.findViewById<EditText>(R.id.etStreet)
        cbCancel = cal.findViewById<Button>(R.id.cbCancel)
        cbOK = cal.findViewById<Button>(R.id.cbOK)
        cbChangeAddress=cal.findViewById<Button>(R.id.cbChangeAddress)

        if (!(lsCompleteAddress.equals(null) || lsCompleteAddress.equals(""))) {

            etBuilding.setText(lsAddline1)

            etStreet.setText(lsAddline2)
        }

        val popup1 = AlertDialog.Builder(context)

        popup1.setView(cal)

        alertdialog = popup1.create()
        alertdialog.setCancelable(true)
        alertdialog.setCanceledOnTouchOutside(true)

        alertdialog.show()
        cbChangeAddress.setOnClickListener {
            autocomplete()
        }
        /*  cbOK.setOnClickListener {
              autocomplete()
          }*/

        cbOK.setOnClickListener() {

            lsAddline1 = etBuilding.text.toString().trim()
            lsAddline2 = etStreet.text.toString().trim()
            var lbProceedAhead = true;


            if (!((lsAddline1 != null && lsAddline1.length > 0))) {
                genMessage(" Flat no,Building");
                lbProceedAhead = false;
            }

            if (!((lsAddline2 != null && lsAddline2.length > 0))) {
                genMessage(" Locality ");
                lbProceedAhead = false;
            }



            if (!(lbProceedAhead) && lsMessage.length > 0) {
                if (lsMessage.length > 50) {
                    TastyToast.makeText(context, "Please enter all details", TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
                } else {
                    TastyToast.makeText(context, "Please enter valid " + lsMessage, TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
                }

            } else {
                lsAddline2 = lsAddline2
                lsCompleteAddress = lsAddline1 + ", " + lsAddline2
                cvaddress.visibility = View.VISIBLE
                tvAddress.setText(lsCompleteAddress).toString().trim()
                alertdialog.dismiss()
                alertdialog.cancel()

            }
        }
        cbCancel.setOnClickListener(View.OnClickListener {

            alertdialog.dismiss()
            alertdialog.cancel()

        })

    }



}
