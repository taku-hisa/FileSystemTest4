
package com.example.filesystemtest4.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.filesystemtest4.UI.MainViewModel
import com.example.filesystemtest4.database.Item
import com.example.filesystemtest4.databinding.FragmentMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

private val category = arrayOf("A","B","C","D","E") //カテゴリを定義
private var CATEGORY_CODE: Int = 42                 //カテゴリの変数

class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ListView
        val adapter = ArrayAdapter(requireContext() , android.R.layout.simple_list_item_1 , category)
        binding.listView.adapter = adapter
        binding.listView.setOnItemClickListener{parent,view,position,id->
            val item = (view.findViewById<TextView>(android.R.id.text1)).text.toString()
            val action = MainFragmentDirections.actionMainFragmentToListFragment(item) //リストの文字を返す
            findNavController().navigate(action)
        }
        //FloatingActionButton
        binding.fab.setOnClickListener{
            //初期選択 category[0]
            CATEGORY_CODE = 0
            AlertDialog.Builder(requireContext()).apply {
                setTitle("SELECT")
                //初期選択 category[0]
                setSingleChoiceItems(category, 0) { _, i ->
                    //選択後 category[i]
                    CATEGORY_CODE = i
                }
                setPositiveButton("OK") { _, _ ->
                    if (CATEGORY_CODE < category.size + 1) {
                        intent()
                    }
                }
                setNegativeButton("Cancel", null)
            }.show()
        }
    }

    //DB登録処理
    private fun intent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        this.startActivityForResult(intent, CATEGORY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (resultCode != RESULT_OK) {
            return
        }else{
            val uri = resultData?.data
            if (uri != null) {
                //一枚選択時の動作
                val inputStream = activity?.contentResolver?.openInputStream(uri)
                //非同期処理
                GlobalScope.launch(Dispatchers.IO) {
                    if (inputStream != null) saveImage(inputStream, 999)
                }
            } else {
                //複数枚選択時の動作
                val clipData = resultData?.clipData
                val clipItemCount = clipData?.itemCount?.minus(1) //エラーになるので、数字を１減らす
                for (i in 0..clipItemCount!!) {
                    val item = clipData.getItemAt(i).uri
                    val inputStream = activity?.contentResolver?.openInputStream(item)
                    //非同期処理
                    GlobalScope.launch(Dispatchers.IO) {
                        if (inputStream != null) saveImage(inputStream, i)
                    }
                }
            }
        }
    }

    private fun saveImage(inputStream: InputStream, int: Int) {
        val date = Date()
        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val name : String = "${sdf.format(date)}_${int}.jpg" //画像の名前
        try {
            ByteArrayOutputStream().use { byteArrOutputStream ->
                activity?.openFileOutput(name, Context.MODE_PRIVATE).use { outStream ->
                    //Bitmapを生成
                    val image = BitmapFactory.decodeStream(inputStream)
                    //Jpegへ変換(quality = 解像度)
                    image.compress(Bitmap.CompressFormat.JPEG, 20, outStream)
                    //画像をアプリ内に保存
                    outStream?.write(byteArrOutputStream.toByteArray())
                    //DB登録
                    val item = Item(0, category[CATEGORY_CODE], name, "")
                    viewModel.insertItem(item)
                    //明示的に閉じる
                    inputStream.close()
                }
            }
        } catch(e:Exception) {
            println("エラー発生")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}