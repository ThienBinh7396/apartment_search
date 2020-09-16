package com.thienbinh.apartmentsearch.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.ActivitySplashBinding
import com.thienbinh.apartmentsearch.db.AppDB
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.db.entities.ApartmentAmenity
import com.thienbinh.apartmentsearch.db.entities.ApartmentType
import com.thienbinh.apartmentsearch.sharePreferences.ApplicationSharedPreference
import com.thienbinh.apartmentsearch.sharePreferences.ApplicationSharedPreferenceModel
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.store.action.ApartmentAction
import com.thienbinh.apartmentsearch.viewModel.SplashActivityViewModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


data class SplashActivityTask(
  var title: String,
  var action: () -> Boolean
)

class SplashActivity : AppCompatActivity(), LifecycleOwner {
  private lateinit var mAppDB: AppDB
  private lateinit var applicationInformation: ApplicationSharedPreferenceModel

  private lateinit var mSplashActivityViewModel: SplashActivityViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash).apply {
      mSplashActivityViewModel =
        ViewModelProvider(this@SplashActivity).get(SplashActivityViewModel::class.java)

      splashActivityViewModel = mSplashActivityViewModel

      lifecycleOwner = this@SplashActivity

    }
  }

  override fun onStart() {
    super.onStart()

    mainAction()
//    goToMainActivity()
  }

  private fun mainAction() {
    val splashActivityTasks = mutableListOf(
      SplashActivityTask(title = "Check first initialize database", checkFirstInitializeDatabase),
      SplashActivityTask(title = "Load data from database", loadDataFromDatabase)
    )

    splashActivityTasks.forEach {
      mSplashActivityViewModel.updateTitle(it.title)
      it.action()
    }
    Log.d("Binh", "Goto")

    goToMainActivity()

  }

  private fun goToMainActivity() {
    Handler(Looper.getMainLooper())
      .postDelayed({
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
      }, 1000)
  }

  private val checkFirstInitializeDatabase: () -> Boolean = {
    run {
      mAppDB = AppDB.getInstance(this@SplashActivity)

      applicationInformation = ApplicationSharedPreference.getApplicationInformation()

      if (!applicationInformation.isFirstDBInitialize) {
        initializeDatabase()

        ApplicationSharedPreference.updateApplicationInformation(
          applicationInformation.copy(
            isFirstDBInitialize = true
          )
        )
      }

      Log.d("Binh", "Check first init")


      return@run true
    }
  }

  private val loadDataFromDatabase: () -> Boolean = {
    run {

      store.dispatch(ApartmentAction.Apartment_ACTION_UPDATE_APARTMENTS(getApartments().toMutableList()))
      store.dispatch(ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_AMENITIES(getApartmentAmenitiesData().toMutableList()))
      store.dispatch(ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_TYPES(getApartmentTypesData().toMutableList()))

      Log.d("Binh", "Check loadDataFromDatabase")
      return@run true
    }
  }

  private fun initializeDatabase() {
    Log.d("Binh", "First initialize database")

    val apartmentTypes = arrayOf(
      ApartmentType(null, "Entire place", "Have a place to yourself"),
      ApartmentType(null, "Private room", "Have your own room and share some common spaces"),
      ApartmentType(
        null,
        "Hotel room",
        "Have a private or shared room in a boutique hotel, hostel, and more"
      ),
      ApartmentType(null, "Shared room", "Stay in a shared space, like a common room"),
    )

    val apartmentAmenities = arrayOf(
      ApartmentAmenity(null, "Wifi", R.drawable.ic_feather_wifi),
      ApartmentAmenity(null, "Coffee maker", R.drawable.ic_feather_coffee),
      ApartmentAmenity(null, "TV", R.drawable.ic_feather_monitor),
      ApartmentAmenity(null, "Washer", R.drawable.ic_feather_speaker),
      ApartmentAmenity(null, "Breakfast", R.drawable.ic_feather_breakfast),
      ApartmentAmenity(null, "Fitness Center", R.drawable.ic_feather_fitness)
    )

    runBlocking {
      mAppDB.getApartmentTypeDAO().insert(
        *apartmentTypes
      )

      mAppDB.getApartmentAmenityDAO().insert(
        *apartmentAmenities
      )

      initializeApartment()
    }
  }

  private fun initializeApartment() {
    val apartmentTypes = getApartmentTypesData()

    val apartments = arrayOf(
      Apartment(
        null,
        title = "Silk Path Boutique Hanoi",
        thumbnail = "https://res.cloudinary.com/do1xjyyru/image/upload/v1600226971/apartment/167935228_wswvko.jpg",
        price = 1924.50f,
        address = "21 Hang Khay, Quận Hoàn Kiếm, Hà Nội",
        longitude = 21.026057f,
        latitude = 105.852726f,
        maximum_adults = 2,
        maximum_children = 2,
        typeId = apartmentTypes[0].id!!,
        description = "Nhìn ra Hồ Hoàn Kiếm, Silk Path Boutique Hanoi cung cấp chỗ nghỉ tại Khu Phố Cổ của thành phố Hà Nội. Trong khuôn viên có quầy bar.\n" +
            "\n" +
            "Mỗi phòng nghỉ tại khách sạn đều được trang bị máy điều hòa và TV truyền hình cáp màn hình phẳng. Một số phòng có khu vực ghế ngồi để thư giãn sau một ngày bận rộn trong khi một số phòng cho tầm nhìn ra hồ nước hoặc thành phố. Các phòng còn đi kèm phòng tắm riêng với vòi sen, dép và đồ vệ sinh cá nhân miễn phí.\n" +
            "\n" +
            "Tại đây có lễ tân phục vụ 24 giờ/ngày.\n" +
            "\n" +
            "Silk Path Boutique Hanoi nằm trong bán kính 200 m từ trung tâm thương mại Tràng Tiền Plaza và 3 km từ Quảng trường Ba Đình. Sân bay gần nhất là sân bay quốc tế Nội Bài, cách đó 22 km.\n" +
            "\n" +
            "Đây là khu vực ở Hà Nội mà khách yêu thích, theo các đánh giá độc lập."
      ),
      Apartment(
        null,
        title = " L' Heritage Hotel Hanoi",
        thumbnail = "https://res.cloudinary.com/do1xjyyru/image/upload/v1600230191/apartment/6048343_tmkmec.jpg",
        price = 1224f,
        address = "39-41 Hang Ga Street, Quận Hoàn Kiếm, Hà Nội",
        longitude = 21.036359f,
        latitude = 105.847117f,
        maximum_adults = 1,
        typeId = apartmentTypes[2].id!!,
        description = "Nằm ở vị trí trung tâm tại Khu Phố Cổ quyến rũ, L' Heritage Hotel Hanoi cách Hồ Hoàn Kiếm và Văn Miếu 5 phút đi bộ. Các phòng máy lạnh hiện đại tại đây được trang bị Wi-Fi miễn phí. Khách sạn còn có phòng xông hơi khô và phòng tập thể dục.\n" +
            "\n" +
            "Tất cả các phòng nghỉ đều có hệ thống ánh sáng ấm áp, sàn gỗ, TV màn hình phẳng và minibar chứa đầy đồ. Một số phòng còn có ban công. Các phòng tắm được trang bị tiện nghi tắm vòi sen.\n" +
            "\n" +
            "L' Heritage cách Phố Đi Bộ nổi tiếng, Nhà hát lớn và Múa rối nước Thăng Long 10 phút lái xe. Sân bay Quốc tế Nội Bài cách đó 45 phút lái xe.\n" +
            "\n" +
            "Bàn đặt tour có thể thu xếp các chuyến đi trong ngày và dịch vụ cho thuê xe hơi. Khách sạn cũng có trung tâm dịch vụ doanh nhân, dịch vụ cho thuê xe đạp và dịch vụ trợ giúp đặc biệt. Dịch vụ đưa đón sân bay được cung cấp kèm phụ phí.\n" +
            "\n" +
            "Nhà hàng phục vụ các món ăn quốc tế và Việt Nam. Bữa sáng tự chọn hàng ngày cũng được phục vụ tại đây.\n" +
            "\n" +
            "Đây là khu vực ở Hà Nội mà khách yêu thích, theo các đánh giá độc lập."
      ),
      Apartment(
        null,
        title = "OYO 276 BH Hotel ",
        thumbnail = "https://res.cloudinary.com/do1xjyyru/image/upload/v1600230740/apartment/258105463_dahrcs.jpg",
        price = 687.550f,
        address = "No. 6, Lane 75, Cau Dat Street, Chuong Duong Do ward, Hanoi, Quận Hoàn Kiếm, Hà Nội",
        longitude = 21.026781f,
        latitude = 105.861361f,
        maximum_adults = 2,
        maximum_children = 3,
        typeId = apartmentTypes[1].id!!,
        description = "OYO 276 BH Hotel cung cấp phòng nghỉ có máy điều hòa và TV màn hình phẳng với truyền hình cáp ở quận Hoàn Kiếm của thành phố Hà Nội. Các tiện nghi của chỗ nghỉ bao gồm dịch vụ lễ tân 24 giờ, phòng khách chung và WiFi miễn phí trong toàn bộ khuôn viên. Khách sạn cách trung tâm thành phố 2,3 km và Nhà thờ Lớn 2,1 km.\n" +
            "\n" +
            "Phòng nghỉ tại khách sạn được trang bị bàn làm việc.\n" +
            "\n" +
            "Các điểm hút khách nổi tiếng gần OYO 276 BH Hotel bao gồm trung tâm thương mại Tràng Tiền Plaza, Nhà hát múa rối nước Thăng Long và Nhà hát Lớn. Sân bay gần nhất là sân bay quốc tế Nội Bài, cách chỗ nghỉ 27 km."
      ),
      Apartment(
        null,
        title = "Comfy and Cozy HomeSTAY nearby Hanoi Opera House",
        thumbnail = "https://res.cloudinary.com/do1xjyyru/image/upload/v1600230952/apartment/170146739_srmyox.jpg",
        price = 387.550f,
        address = "969 Hồng Hà, Quận Hoàn Kiếm, Hà Nội",
        longitude = 21.023096f,
        latitude = 105.861189f,
        maximum_adults = 4,
        typeId = apartmentTypes[1].id!!,
        description = "Tọa lạc tại thành phố Hà Nội, gần Nhà Hát Lớn và trung tâm thương mại Tràng Tiền Plaza, omfy and Cozy HomeSTAY nearby Hanoi Opera House cung cấp chỗ nghỉ với WiFi cùng chỗ đỗ xe riêng miễn phí.\n" +
            "\n" +
            "Phòng nghỉ tại đây có sân hiên, máy điều hòa, TV màn hình phẳng và phòng tắm riêng đi kèm vòi sen cùng máy sấy tóc. Để thêm phần thuận tiện cho du khách, chỗ nghỉ có thể cung cấp khăn tắm và ga trải giường với một khoản phụ phí.\n" +
            "\n" +
            "Nhà hát múa rối nước Thăng Long và Nhà thờ Lớn cách homestay này lần lượt 1,9 km và 2,1 km. Sân bay gần nhất là sân bay quốc tế Nội Bài, cách 27 km từ Comfy and Cozy HomeSTAY nearby Hanoi Opera House.\n" +
            "\n" +
            "Đây là khu vực ở Hà Nội mà khách yêu thích."
      ),
      Apartment(
        null,
        title = "HOÀNG GIA MOTEL",
        thumbnail = "https://res.cloudinary.com/do1xjyyru/image/upload/v1600236322/apartment/169269844_cpds2r.jpg",
        price = 380.550f,
        address = "40 Nhân Mỹ, Mỹ Đình 2, Từ Liêm, Hà Nội",
        longitude = 21.023572f,
        latitude = 105.770866f,
        maximum_adults = 2,
        typeId = apartmentTypes[3].id!!,
        description = "Tọa lạc tại thành phố Hà Nội, cách Sân vận động Mỹ Đình 1,1 km và Bảo tàng Dân tộc học Việt Nam 5 km, HOÀNG GIA MOTEL cung cấp chỗ nghỉ với sân hiên cũng như chỗ đỗ xe riêng miễn phí cho khách lái xe. Khách sạn này nằm cách trung tâm thương mại Vincom Center Nguyễn Chí Thanh khoảng 7 km, Chùa Một Cột 8 km và Bảo tàng Mỹ thuật Việt Nam 9 km. Chỗ nghỉ có lễ tân 24 giờ, dịch vụ đưa đón sân bay, dịch vụ phòng và WiFi miễn phí.\n" +
            "\n" +
            "Phòng nghỉ tại HOÀNG GIA MOTEL được trang bị ấm đun nước, máy điều hòa và TV màn hình phẳng.\n" +
            "\n" +
            "Lăng Chủ tịch Hồ Chí Minh và Văn Miếu - Quốc Tử Giám đều nằm trong bán kính 9 km từ khách sạn. Sân bay gần nhất là sân bay quốc tế Nội Bài, cách HOÀNG GIA MOTEL 27 km.."
      ),
      Apartment(
        null,
        title = "Sofitel Legend Metropole Hanoi",
        thumbnail = "https://res.cloudinary.com/do1xjyyru/image/upload/v1600236475/apartment/251755190_iks5zi.jpg",
        price = 787.550f,
        address = "15 Ngo Quyen Street, Quận Hoàn Kiếm, Hà Nội, Việt Nam",
        longitude = 21.025810f,
        latitude = 105.856887f,
        maximum_adults = 4,
        typeId = apartmentTypes[0].id!!,
        description = "Là địa danh lịch sử sang trọng từ năm 1901, Sofitel Legend Metropole có các dịch vụ spa thư giãn, dịch vụ phòng 24 giờ và hồ bơi nước nóng. Khách sạn tọa lạc ở trung tâm thủ đô Hà Nội, gần Khu Phố Cổ.\n" +
            "\n" +
            "Khách sạn 5 sao Sofitel Legend Metropole chỉ cách Nhà hát Lớn Hà Nội nổi tiếng 160 m. Hồ Hoàn Kiếm thơ mộng cách khách sạn chưa đầy 500 m.\n" +
            "\n" +
            "Vẫn giữ được nét lộng lẫy ban đầu của khách sạn, các phòng nghỉ tại đây mang chủ đề tân cổ điển thanh lịch hoặc có lối trang trí kiểu Pháp cổ điển. Mỗi phòng nghỉ đều được trang bị máy điều hòa, Internet miễn phí và TV màn hình phẳng.\n" +
            "\n" +
            "Là nơi du khách có thể tận hưởng dịch vụ mát-xa và chăm sóc toàn thân, Le Spa du Metropole cung cấp kết hợp các liệu pháp kiểu phương Đông và phương Tây. Trung tâm Thể dục So Fit có đầy đủ trang thiết bị cũng như các lớp tập thể dục. Khách sạn còn có trung tâm dịch vụ doanh nhân và cửa hàng quà tặng.\n" +
            "\n" +
            "Nhà hàng Spices Garden phục vụ các món đặc sản châu Á cũng như Việt Nam và có phòng ăn riêng và sân hiên ngoài trời. Các lựa chọn ăn uống khác bao gồm Angelina, quầy bar cocktail, sảnh whiskey và nhà hàng dành riêng của Hà Nội cũng như nhà hàng Pháp đầu tiên tại Hà Nội, Le Beaulieu."
      ),
      Apartment(
        null,
        title = "A25 Hàng Than Hotel",
        thumbnail = "https://res.cloudinary.com/do1xjyyru/image/upload/v1600236657/apartment/60663271_gvhvky.jpg",
        price = 187.550f,
        address = "15 Hàng Than, Nguyễn Trung Trực, Ba Đình, Hà Nội",
        longitude = 21.042709f,
        latitude = 105.847547f,
        maximum_children = 1,
        typeId = apartmentTypes[1].id!!,
        description = "Tọa lạc ở thành phố Hà Nội, cách Chợ Đồng Xuân chưa đến 1 km, A25 Hàng Than Hotel có chỗ nghỉ lắp máy điều hòa và sảnh khách chung. Trong số các tiện nghi của chỗ nghỉ này còn có nhà hàng, lễ tân 24 giờ, dịch vụ phòng và WiFi miễn phí trong toàn bộ khuôn viên. Khách sạn này cung cấp các phòng gia đình.\n" +
            "\n" +
            "Tất cả phòng nghỉ tại A25 Hàng Than Hotel đều được trang bị bàn làm việc, TV màn hình phẳng, phòng tắm riêng và khu vực ghế ngồi.\n" +
            "\n" +
            "Chỗ nghỉ phục vụ bữa sáng à la carte.\n" +
            "\n" +
            "A25 Hàng Than Hotel có sân hiên.\n" +
            "\n" +
            "Các điểm tham quan nổi tiếng gần khách sạn bao gồm Ô Quan Chưởng, Đền Quán Thánh và Hồ Hoàn Kiếm. Sân bay gần nhất là sân bay quốc tế Nội Bài, cách đó 24 km, và A25 Hàng Than Hotel cung cấp dịch vụ đưa đón sân bay với một khoản phụ phí.\n"
      ),
      Apartment(
        null,
        title = "The Poppy Villa & Hotel",
        thumbnail = "https://res.cloudinary.com/do1xjyyru/image/upload/v1600236657/apartment/60663271_gvhvky.jpg",
        price = 333.550f,
        address = "12 Ngõ Bà Triệu, Lê Đại Hành, Hai Bà Trưng, Hà Nội",
        longitude = 21.012417f,
        latitude = 105.849034f,
        typeId = apartmentTypes[2].id!!,
        description = "Tọa lạc tại thành phố Hà Nội, cách trung tâm thương mại Tràng Tiền Plaza trong vòng 2 km, The Poppy Villa & Hotel có dịch vụ nhận phòng/trả phòng cấp tốc, phòng nghỉ không gây dị ứng, sảnh khách chung, WiFi miễn phí và sân hiên. Khách sạn 3 sao này còn có dịch vụ concierge và bàn đặt tour. Chỗ nghỉ cung cấp dịch vụ lễ tân 24 giờ, dịch vụ phòng và dịch vụ thu đổi ngoại tệ cho khách.\n" +
            "\n" +
            "Phòng nghỉ của khách sạn được trang bị máy điều hòa, truyền hình cáp màn hình phẳng, tủ lạnh, ấm đun nước, chậu rửa vệ sinh (bidet), máy sấy tóc và bàn làm việc. Các phòng còn có phòng tắm riêng với vòi sen cùng đồ vệ sinh cá nhân miễn phí và tầm nhìn ra quang cảnh thành phố. Tủ để quần áo cũng được bố trí trong tất cả các phòng.\n" +
            "\n" +
            "The Poppy Villa & Hotel phục vụ bữa sáng à la carte hoặc kiểu Mỹ.\n" +
            "\n" +
            "Khách sạn nằm cách Nhà Hát Lớn Hà Nội 2,6 km và Nhà hát múa rối nước Thăng Long 2,8 km. Sân bay gần nhất là sân bay quốc tế Nội Bài, cách đó 28 km, và The Poppy Villa & Hotel cung cấp dịch vụ đưa đón sân bay với một khoản phụ phí."
      )
    )


    runBlocking {
      mAppDB.getApartmentDAO().insert(*apartments)
    }
  }

  private fun getApartments() = mAppDB.getApartmentDAO().getApartmentAmenitiesSynchronous()

  private fun getApartmentTypesData() =
    mAppDB.getApartmentTypeDAO().getApartmentTypeListSynchronous()

  private fun getApartmentAmenitiesData() =
    mAppDB.getApartmentAmenityDAO().getApartmentAmenitiesSynchronous()
}