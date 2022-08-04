package com.lxj.androidktxdemo.fragment


import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Color
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.core.*
import com.lxj.androidktx.util.CountDownWorker
import com.lxj.androidktx.util.XHtml
import com.lxj.androidktx.widget.TabBar
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_view_ext.*
import org.xml.sax.XMLReader
import java.lang.reflect.Field
import java.util.*
import kotlin.random.Random

class ViewExtPage : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_view_ext

    val countDownWorker: CountDownWorker by lazy {
        CountDownWorker(this, onChange = {
            text1.text = "countDown: $it"
        })
    }

    override fun initView() {
        XHtml.preferSizeUnit(TypedValue.COMPLEX_UNIT_SP)
        val text =  "分享成功了<font color='#FF0000' fontSize='54'>32个</font>内容\n有<font color='#FFEE90' font-size='44'>1个</font>好友下单\n获得奖励<font color='#FFEE90' size='14'>11积分</font>"
        tvHtml.text = XHtml.fromHtml(text)
        val value = dp2px(150f)
        text1.width(value)
        text1.text = "自定义字体：text1.width($value)"
        text1.click {
            ImageUtils.save2Album(nsv.toBitmap(), Bitmap.CompressFormat.PNG)
//            PermissionUtils.permission(PermissionConstants.STORAGE)
//                .callback(object : PermissionUtils.SimpleCallback{
//                    override fun onGranted() {
//                        ExoPlayerManager.playSingle("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES)}/uRingtone/Boquita-1653673313539.mp3")
//                    }
//
//                    override fun onDenied() {
//                    }
//                })
//                .request()
//            ToastUtils.showShort("click " + Random.nextInt(1000000))
//            val vc = TransitionSet()
//            vc.addTransition(ChangeBounds())
//            vc.addTransition(ChangeTransform())
//            vc.setDuration(1000)
//            vc.propagation = CircularPropagation()
//            TransitionManager.beginDelayedTransition(text1.parent as ViewGroup, vc)
//            countDownWorker.start()

//            text1.width(Random.nextInt(ScreenUtils.getAppScreenWidth()))
            text1.animateWidth(Random.nextInt(ScreenUtils.getAppScreenWidth()), duration = 1000)
        }


        text2.widthAndHeight(value, value)
        text2.text = "text2.widthAndHeight($value,$value)"
        text2.click {

            ToastUtils.showShort("click2222 " + Random.nextInt(1000000))
        }

        text3.margin(leftMargin = value)
        text3.text = "text3.margin(leftMargin = $value)"



        text3.click {
            it.animateWidthAndHeight(600, 900, action = {

            })
        }

        text4.post {
            image2.setImageBitmap(text4.toBitmap())
        }
        text4.click { ToastUtils.showShort("clicked") }

//        tvVerify.mSolid = Color.parseColor("#ff0000")
        tvVerify.click {
            tvVerify.start()
        }

        verifyCodeInput.onInputFinish = {
            ToastUtils.showLong(it)
        }
        tabbarRadio.setTabs(
            listOf(
                TabBar.Tab(
                    text = "啊啊",
                    selectedIconRes = R.mipmap.checked,
                    normalIconRes = R.mipmap.uncheck
                ),
                TabBar.Tab(
                    text = "大萨达",
                    selectedIconRes = R.mipmap.checked,
                    normalIconRes = R.mipmap.uncheck
                ),
                TabBar.Tab(
                    text = "Free",
                    selectedIconRes = R.mipmap.checked,
                    normalIconRes = R.mipmap.uncheck
                ),
            )
        )
        ivDemo.load(R.mipmap.h, roundRadius = 12.dp)
        tabbar.setTabs(
            listOf(
                TabBar.Tab(text = "Home"),
                TabBar.Tab(text = "Category"),
                TabBar.Tab(text = "Message"),
                TabBar.Tab(text = "My"),
//                TabBar.Tab( selectedIconRes = R.mipmap.ic_launcher_round),
//                TabBar.Tab( selectedIconRes = R.mipmap.ic_launcher_round)
            )
        ) { position ->
            ToastUtils.showShort("选择了：" + position)
            (0..3).forEach {
                tabbar.getChildAt(it).background = if (position == it) createDrawable(
                    strokeWidth = 2.pt,
                    strokeColor = Color.parseColor("#FF4B76DB")
                ) else null
            }
            true
        }

        mtv.setup("床前明月光")
        mtv2.setup("床前明月光，疑是地上霜；举头望明月，低头思故乡。")

        val list = listOf(
            "http://sealbox.oss-ap-southeast-1.aliyuncs.com/upload/20210830/27f4e97272474d38299240aa2c6f5678.png",
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic33.nipic.com%2F20130924%2F12085979_105431188100_2.jpg&refer=http%3A%2F%2Fpic33.nipic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1634003717&t=1d514ffc2294afa6a28c6f5df20702b7",
        )
        banner.adapter = CommonBannerAdapter(list, cornerRadius = 10.dp)
    }

    override fun initData() {
        super.initData()

//        mtv2.setup(loop = false)
        postDelay(2000) {
            mtv.startScroll()
            mtv2.startScroll()
            marqueeLayout.startScroll()
        }

        val animator = ValueAnimator.ofFloat(0f, 100f)
        animator.addUpdateListener {
            progressBar?.progress = 100f - it.animatedValue as Float
        }
        animator.setDuration(10000)
        animator.start()
    }

    class HtmlSizeTag(private val tagName: String) : Html.TagHandler {
        // 存放标签所有属性键值对
        val attributes: HashMap<String, String> = HashMap()
        private var startIndex = 0
        private var endIndex = 0
        override fun handleTag(
            opening: Boolean,
            tag: String,
            output: Editable,
            xmlReader: XMLReader
        ) {
            if (tag.lowercase(Locale.getDefault()) == tagName) {
                // 解析所有属性值
                parseAttributes(xmlReader)
                if (opening) {
                    startIndex = output.length
                } else {
                    endEndHandleTag(output, xmlReader)
                }
            }
        }

        fun endEndHandleTag(output: Editable, xmlReader: XMLReader?) {
            endIndex = output.length
            LogUtils.e("attr: ${attributes.toJson()}")
            // 获取对应的属性值
            val color = attributes["color"]
            var size = attributes["fontSize"]
//            size = size!!.split("px".toRegex()).toTypedArray()[0]

            // 设置颜色
            if (!TextUtils.isEmpty(color)) {
                output.setSpan(
                    ForegroundColorSpan(Color.parseColor(color)), startIndex, endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            // 设置字体大小
            if (!TextUtils.isEmpty(size)) {
                output.setSpan(
                    AbsoluteSizeSpan(size!!.toInt()), startIndex, endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        private fun parseAttributes(xmlReader: XMLReader) {
            try {
                val elementField: Field = xmlReader.javaClass.getDeclaredField("theNewElement")
                elementField.setAccessible(true)
                val element: Any = elementField.get(xmlReader)
                val attsField: Field = element.javaClass.getDeclaredField("theAtts")
                attsField.setAccessible(true)
                val atts: Any = attsField.get(element)
                val dataField: Field = atts.javaClass.getDeclaredField("data")
                dataField.setAccessible(true)
                val data = dataField.get(atts) as Array<String>
                val lengthField: Field = atts.javaClass.getDeclaredField("length")
                lengthField.setAccessible(true)
                val len = lengthField.get(atts) as Int
                for (i in 0 until len) {
                    attributes[data[i * 5 + 1]] = data[i * 5 + 4]
                }
            } catch (e: Exception) {
            }
        }
    }
}