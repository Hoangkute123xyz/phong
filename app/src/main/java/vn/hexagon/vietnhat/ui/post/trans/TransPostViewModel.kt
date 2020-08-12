package vn.hexagon.vietnhat.ui.post.trans

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseViewModel
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.base.utils.addToCompositeDisposable
import vn.hexagon.vietnhat.base.utils.applyScheduler
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.PostDetailResponse
import vn.hexagon.vietnhat.data.model.detail.Images
import vn.hexagon.vietnhat.data.model.product.Product
import vn.hexagon.vietnhat.data.model.product.ProductResponse
import vn.hexagon.vietnhat.data.model.translator.PostResponse
import vn.hexagon.vietnhat.data.remote.NetworkState
import vn.hexagon.vietnhat.repository.posts.common.PostCreateRepository
import java.io.File
import javax.inject.Inject

/**
 * Created by VuNBT on 9/14/2019.
 */
class TransPostViewModel @Inject constructor(private val repository: PostCreateRepository) :
    MVVMBaseViewModel() {
    // Translate response
    val transPostResponse = MutableLiveData<PostResponse>()

    // Translate edit response
    val transEditPostResponse = MutableLiveData<PostResponse>()

    // Translate detail response
    val transDetailResponse = MutableLiveData<PostDetailResponse>()

    // Request file
    private var requestFile = ArrayList<MultipartBody.Part?>()

    // Product list response
    val productPostResponse = MutableLiveData<ProductResponse>()

    // Product list object
    private var productList = ArrayList<RequestBody>()

    // Request file
    private var imgList = ArrayList<MultipartBody.Part?>()

    var title = Constant.BLANK
    var address = Constant.BLANK
    var price = Constant.BLANK
    var phone = Constant.BLANK
    var content = Constant.BLANK
    var note = Constant.BLANK
    var putOnTop: Boolean = false

    /**
     * Get response from server
     *
     * @param userId
     * @param serviceId
     * @param title
     * @param address
     * @param price
     * @param transType
     * @param phone
     * @param content
     * @param note
     * @param isTop
     * @param imgSwipeList
     */
    fun sendDataPost(
        userId: String,
        serviceId: String,
        title: String,
        address: String,
        price: String,
        transType: String,
        phone: String,
        content: String,
        note: String,
        isTop: String,
        imgSwipeList: ArrayList<String>,
        products: ArrayList<Product>
    ) {

        // Add mutable map, upload fields data
        val params = mutableMapOf<String, RequestBody>()
        params.apply {
            put("user_id", requestBody(userId))
            put("service_id", requestBody(serviceId))
            put("title", requestBody(title))
            put("address", requestBody(address))
            put("price", requestBody(price))
            put("translate_type", requestBody(transType))
            put("phone", requestBody(phone))
            put("content", requestBody(content))
            put("note", requestBody(note))
            put("is_top", requestBody(isTop))
        }

        // Rewrite product img name (must match with image file name)
        val newProducts = ArrayList<Product>()
        products.forEach {
            val imageName = File(it.img).name
            DebugLog.e("Image name: $imageName")
            val product = Product(it.name, it.price, imageName)
            newProducts.add(product)
        }
        productList.clear()
        productList.add(requestBody(Gson().toJson(newProducts).toString()))

        // Get products
        for (i in 0 until products.size) {
            // Add new image for upload
            imgList.add(prepareImageForRequest("img_product[]", File(products[i].img)))
        }

        /////////////////// Image cover //////////////////////
        // Get image name
        val coverImages = ArrayList<RequestBody>()
        val imagesListObject = ArrayList<Images>()
        imgSwipeList.forEach {
            val imageName = File(it).name
            DebugLog.e("Image name upload: $imageName")
            val images = Images(null, imageName)
            imagesListObject.add(images)
            // Add new image for upload
            requestFile.add(prepareImageCoverForRequest("img[]", File(it)))
        }
        // Upload cover images JSON Object
        coverImages.clear()
        coverImages.add(requestBody(Gson().toJson(imagesListObject).toString()))

        // Request data
        networkState.postValue(NetworkState.LOADING)
        // Send request
        repository.sendDataPostObj(params, productList, imgList, coverImages, requestFile)
            .applyScheduler()
            .subscribe(
                { result ->
                    networkState.postValue(NetworkState.LOADED)
                    transPostResponse.postValue(result)
                },
                { throwable ->
                    networkState.postValue(NetworkState.ERROR)
                    DebugLog.e(throwable.message.toString())
                }
            ).addToCompositeDisposable(compositeDisposable)
    }

    /**
     * Request update data
     *
     * @param userId
     * @param postId
     * @param title
     * @param address
     * @param price
     * @param transType
     * @param phone
     * @param content
     * @param note
     * @param isTop
     * @param imgSwipeList
     */
    fun editDataPost(
        userId: String,
        postId: String,
        title: String,
        address: String,
        price: String,
        transType: String,
        phone: String,
        content: String,
        note: String,
        isTop: String,
        products: ArrayList<Product>,
        imgSwipeList: ArrayList<String>
    ) {

        // Product list object
        val productList = ArrayList<RequestBody>()

        // Add mutable map, upload fields data
        val params = mutableMapOf<String, RequestBody>()
        params.apply {
            put("user_id", requestBody(userId))
            put("post_id", requestBody(postId))
            put("title", requestBody(title))
            put("address", requestBody(address))
            put("price", requestBody(price))
            put("translate_type", requestBody(transType))
            put("phone", requestBody(phone))
            put("content", requestBody(content))
            put("note", requestBody(note))
            put("is_top", requestBody(isTop))
        }

        /////////////////// Products //////////////////////
        // Rewrite product img name (must match with image file name)
        val newProducts = ArrayList<Product>()
        products.forEach {
            val imageName = File(it.img).name
            DebugLog.e("Image name upload: ${it.img}")
            val product = Product(it.name, it.price, imageName)
            newProducts.add(product)
        }
        productList.clear()
        productList.add(requestBody(Gson().toJson(newProducts).toString()))

        // Get products
        for (i in 0 until products.size) {
            // Check image uploaded then skip it
            val regex = products[i].img.contains("chapp.vn")
            if (regex) continue
            // Add new image for upload
            imgList.add(prepareImageForRequest("img_product[]", File(products[i].img)))
        }

        /////////////////// Image cover //////////////////////
        // Get image name
        val coverImages = ArrayList<RequestBody>()
        val imagesListObject = ArrayList<Images>()
        imgSwipeList.forEach {
            val imageName = File(it).name
            DebugLog.e("Image name upload: $imageName")
            val images = Images(null, imageName)
            imagesListObject.add(images)
        }
        // Upload cover images JSON Object
        coverImages.clear()
        coverImages.add(requestBody(Gson().toJson(imagesListObject).toString()))
        // Upload cover image file
        for (i in 0 until imgSwipeList.size) {
            // Check image uploaded then skip it
            val regex = imgSwipeList[i].contains("chapp.vn")
            if (regex) continue
            // Add new image for upload
            requestFile.add(prepareImageCoverForRequest("img[]", File(imgSwipeList[i])))
        }
        // Request data
        networkState.postValue(NetworkState.LOADING)
        repository.editPostProductRequest(
            params,
            productList,
            imgList,
            coverImages,
            requestFile
        )
            .applyScheduler()
            .subscribe(
                { result ->
                    networkState.postValue(NetworkState.LOADED)
                    transEditPostResponse.postValue(result)
                },
                { throwable ->
                    networkState.postValue(NetworkState.ERROR)
                    DebugLog.e(throwable.message.toString())
                }
            ).addToCompositeDisposable(compositeDisposable)
    }

    /**
     * Get detail post
     *
     * @param userId
     * @param postId
     */
    fun getDetailPost(userId: String, postId: String) {
        // Start loading
        networkState.postValue(NetworkState.LOADING)
        // Send request
        repository.getDetailPost(userId, postId)
            .applyScheduler()
            .subscribe(
                { result ->
                    networkState.postValue(NetworkState.LOADED)
                    transDetailResponse.postValue(result)
                },
                { throwable ->
                    networkState.postValue(NetworkState.ERROR)
                    DebugLog.e(throwable.message.toString())
                }
            ).addToCompositeDisposable(compositeDisposable)
    }

    /**
     * Get product list
     *
     * @param postId
     */
    fun getProductList(postId: String) {
        networkState.postValue(NetworkState.LOADING)
        repository.getProductList(postId)
            .applyScheduler()
            .subscribe(
                { result ->
                    networkState.postValue(NetworkState.LOADED)
                    productPostResponse.postValue(result)
                },
                { throwable ->
                    networkState.postValue(NetworkState.ERROR)
                    DebugLog.e(throwable.message.toString())
                }
            ).addToCompositeDisposable(compositeDisposable)
    }
}