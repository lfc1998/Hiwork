package hiwork.app.com;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hiwork.app.org.Student;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import hiwork.app.R;

public class StudentsActivity extends Activity {
	String ip = "192.168.1.103";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_students);
		networkPolicy();
		// 获得Layout里面的ListView
		ListView list = (ListView) findViewById(R.id.student_listView1);
		// 生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(
				this,
				getStudentsData(""),
				R.layout.student_item,
				new String[] { "img", "name", "id" },
				new int[] { R.id.studentItem_imageView1,
						R.id.studentItem_textView1, R.id.studentItem_textView2 });

		// 添加并且显示
		list.setAdapter(listItemAdapter);
		//添加单击监听  
		list.setOnItemClickListener(new OnItemClickListener() {  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	Map<String, Object> clkmap = (Map<String, Object>) arg0.getItemAtPosition(arg2);
            	setTitle(clkmap.get("name").toString());  
            }
        }); 
	}

	// 生成多维动态数组，并加入数据
	private List<Map<String, Object>> getData() {
		ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.girl);
		map.put("name", "张小丽");
		map.put("id", "20130201023");
		listitem.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.boy);
		map.put("name", "李军");
		map.put("id", "20130201024");
		listitem.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.boy);
		map.put("name", "马云飞");
		map.put("id", "20130201025");
		listitem.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.girl);
		map.put("name", "刘艳");
		map.put("id", "20130201026");
		listitem.add(map);

		return listitem;
	}

	private List<Map<String, Object>> getStudentsData(String str) {
		List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
		// mData.clear();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Student> students = getStudents(str);
		for (Student st : students) {
			map = new HashMap<String, Object>();
			if (st.getSex().trim().equals("m"))
				map.put("img", R.drawable.boy);
			else
				map.put("img", R.drawable.girl);
			map.put("name", st.getStudentName());
			map.put("id", st.getStudentId());
			mData.add(map);
		}
		return mData;
	}

	public List<Student> getStudents(String queryStr) {
		List<Student> questions = new ArrayList<Student>();
		URL url = null;
		try {
			url = new URL("http://" + ip + ":8080/homeworkServer/servlet/getStudents");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setRequestMethod("POST");
			DataOutputStream outobj = new DataOutputStream(
					connection.getOutputStream());

			outobj.writeUTF(queryStr);
			outobj.flush();
			outobj.close();
			ObjectInputStream ois = new ObjectInputStream(
					connection.getInputStream());

			Student obj = (Student) ois.readObject();
			while (!obj.getStudentId().equals("00000")) {
				// System.out.println(obj.getQuestion());
				questions.add(obj);
				obj = (Student) ois.readObject();
			}
			ois.close();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return questions;
	}

	public void networkPolicy() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																		// 就包括了磁盘读写和网络I/O
				.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
	}
}
