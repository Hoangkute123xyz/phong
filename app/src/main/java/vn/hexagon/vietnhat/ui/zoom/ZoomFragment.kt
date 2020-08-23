package vn.hexagon.vietnhat.ui.zoom

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_zoom.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.databinding.FragmentZoomBinding
import vn.hexagon.vietnhat.ui.detail.PostDetailViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/*
 * Create by VuNBT on 2019-12-25 
 */
class ZoomFragment : MVVMBaseFragment<FragmentZoomBinding, PostDetailViewModel>() {
    // View model
    private lateinit var viewModel: PostDetailViewModel

    // Array Image
    private var url = ""

    // Position image pager selected by user
    private var mPosition = -1

    // Action bar
    private val actionBar: SimpleActionBar? by lazy {
        baseActionBar as? SimpleActionBar
    }

    private lateinit var scope: CoroutineScope

    override fun getBaseViewModel(): PostDetailViewModel {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[PostDetailViewModel::class.java]
        return viewModel
    }

    override fun getBindingVariable(): Int = BR.viewmodel

    override fun initData(argument: Bundle?) {

    }

    override fun isShowActionBar(): View? = SimpleActionBar(activity)

    override fun isActionBarOverlap(): Boolean = false

    override fun isShowBottomNavigation(): Boolean = false

    override fun getLayoutId(): Int = R.layout.fragment_zoom

    override fun initView() {
        scope = CoroutineScope(Dispatchers.Default)
        actionBar?.apply {
            leftButtonVisible = true
            leftActionBarButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        arguments?.let {
            url=ZoomFragmentArgs.fromBundle(it).uri[0]
            mPosition = ZoomFragmentArgs.fromBundle(it).pos
        }
        activity?.let { context ->
            val imgAdapter = ImagePagerAdapter(context,url)
            zoomPager.adapter = imgAdapter
            tvDownload.setOnClickListener {
                scope.launch {
                    val responseAsync = async {
                        val client = OkHttpClient.Builder().build()
                        val request =
                            Request.Builder().url(url).get().build()
                        return@async client.newCall(request).execute()
                    }
                    val response = responseAsync.await()
                    response.body()!!.byteStream().writeFile()
                }
            }
        }
    }

    private suspend fun InputStream.writeFile() = use { input ->
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (!dir.exists())
            dir.mkdir()
        val file = File(dir.absolutePath, "IMG_${System.currentTimeMillis()}.jpg")
        val output = FileOutputStream(file)
        output.use { output ->
            val buffer = ByteArray(2024 * 4)
            var read = input.read(buffer)
            var data = read
            while (read != -1) {
                output.write(buffer, 0, read)
                data += read
                read = input.read(buffer)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    R.string.download_image_successfully,
                    Toast.LENGTH_SHORT
                ).show()
            }
            output.flush()
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun initAction() {
    }

}