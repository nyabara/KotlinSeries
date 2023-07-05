package com.example.kotlinseries.ui
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import com.example.kotlinseries.R
import com.example.kotlinseries.viewmodels.AddHouseViewModel

const val KEY_EVENT_ACTION_IL = "key_event_action"
const val KEY_EVENT_EXTRA_IL = "key_event_extra"
class HouseSelectorAttr @JvmOverloads
constructor(
    context: Context, attributeSet: AttributeSet? = null,
    defStyle: Int = 0, defRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle, defRes), AdapterView.OnItemSelectedListener {


    //our variables that will hold values listened
    private var spinnerHouseTypesAdapt: Spinner
    private var spinnerElectricityType: Spinner
    private var hasWaterCheck: CheckBox
    private var hasWatchmanCheck: CheckBox
    private var ownCompoundCheck: CheckBox
    private var uploadImages:TextView
    private var selectedHouseType: String? = null
    private var selectedElectricityType: String? = null
    private var houseTypes = listOf("Single Room", "One Bedroom", "Double Room")
    private var electricityTypes = listOf("Paid", "Prepaid")
    private var flags = listOf(R.drawable.buy_rent, R.drawable.for_rent, R.drawable.house)
    private var haswater: Boolean = false
    private var haswatchman: Boolean = false
    private var owncompound: Boolean = false

    //initializing the views before oncreate
    init {
        //inflating the layout
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflator.inflate(R.layout.house_selector_attr, this)

        hasWaterCheck = findViewById(R.id.hasWater)
        hasWatchmanCheck = findViewById(R.id.hasWatchman)
        ownCompoundCheck = findViewById(R.id.ownCompound)
        spinnerHouseTypesAdapt = findViewById(R.id.spinnerHouseTypes)
        spinnerElectricityType = findViewById(R.id.spinnerElectricityTypes)
        uploadImages = findViewById(R.id.uploadImages)

        //Listening to events on our views
        hasWaterCheck.setOnClickListener {
            //hasWaterCheck.isChecked = haswater

            if (hasWaterCheck.isChecked) {
                haswater = true

            } else {
                haswater = false

            }
        }
        hasWatchmanCheck.setOnClickListener {
            if (hasWatchmanCheck.isChecked) {
                haswatchman = true
            } else {
                haswatchman = false
            }
        }
        ownCompoundCheck.setOnClickListener {

            if (ownCompoundCheck.isChecked) {
                owncompound = true

            } else {
                owncompound = false

            }
        }

        spinnerHouseTypesAdapt.setOnItemSelectedListener(this)
        //val customSpinnerAdapter = CustomSpinnerAdapter(context, flags, houseTypes)
        val arraySpinnerAdapter = ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,houseTypes)
        arraySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHouseTypesAdapt.adapter = arraySpinnerAdapter

        spinnerElectricityType.setOnItemSelectedListener(this)
//        val customSpinnerAdapter1 = CustomSpinnerAdapter(context, flags, electricityTypes)
//        spinnerElectricityType.adapter = customSpinnerAdapter1
        val arraySpinnerAdapterElectricity = ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,electricityTypes)
        arraySpinnerAdapterElectricity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerElectricityType.adapter = arraySpinnerAdapterElectricity


        uploadImages.setOnClickListener {
//            val keyCode = 1
//            val intent = Intent(KEY_EVENT_ACTION_IL).apply { putExtra(KEY_EVENT_EXTRA_IL, keyCode) }
//            LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent)
            findNavController().navigate(R.id.action_filesFragment_to_cameraFragment)
        }

    }

    //Listens to house attributes selected UI
    private var houseSelectorListener: ((String, String, Boolean, Boolean, Boolean) -> Unit?)? =
        null

    //called in our fragment to pass values listened to
    fun setListener(selector: (String, String, Boolean, Boolean, Boolean) -> Unit) {

        this.houseSelectorListener = selector

    }

    //Converting houseType list to map so that we can get the position of the current selected item
    private fun getHouseTypesAsMap(houseTypes: List<String>): HashMap<String, Int> {
        val mapHouseTypes = HashMap<String, Int>()
        for (houseType in houseTypes) {
            mapHouseTypes.set(houseType, houseTypes.indexOf(houseType))
        }
        return mapHouseTypes
    }

    private fun getElectricityTypesAsMap(electricityTypes: List<String>):HashMap<String,Int>{
        val mapElectricityTypes = HashMap<String,Int>()
        for (electricityType in electricityTypes){
            mapElectricityTypes.set(electricityType,electricityTypes.indexOf(electricityType))
        }
        return mapElectricityTypes
    }

    //Call this to setListener with values
    private fun broadcastSearch() {

        val houseTypeId = selectedHouseType
        val electricityTypeId = selectedElectricityType
        //this.searchSelectorListener?.onSearchSelected(kilometer,hasWater, hasWatchMan)
        this.houseSelectorListener?.let { function ->
            function(houseTypeId!!, electricityTypeId!!, haswater, haswatchman, owncompound)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (view) {
            spinnerHouseTypesAdapt -> selectedHouseType = houseTypes[position]
            spinnerElectricityType -> selectedElectricityType = electricityTypes[position]
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    //updates the ui from cached data
    fun setSelectedSearch(
        houseType: String,
        electricity: String,
        hasWater: Boolean,
        hasWatchman: Boolean,
        owncoMpound: Boolean
    ) {

        val mapHouseTypes = getHouseTypesAsMap(houseTypes)
        val housetype = mapHouseTypes.get(houseType)
        val houseposition = mapHouseTypes.values.indexOf(housetype)

        val mapElectricityTYpes = getElectricityTypesAsMap(electricityTypes)
        val electricityType = mapElectricityTYpes.get(electricity)
        val electricityPosition = mapElectricityTYpes.values.indexOf(electricityType)

        haswater = hasWater
        haswatchman = hasWatchman
        owncompound = owncoMpound
        hasWaterCheck.isChecked = haswater
        hasWatchmanCheck.isChecked = haswatchman
        ownCompoundCheck.isChecked = owncompound

        spinnerHouseTypesAdapt.setSelection(houseposition)
        spinnerElectricityType.setSelection(electricityPosition)

    }
}