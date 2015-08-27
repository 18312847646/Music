package midsummer.music;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class InitializeView extends AppCompatActivity
{
	// 初始化toolbar
	@ViewById
	public Toolbar toolbar;
	// 初始化侧边栏
	@ViewById
	public DrawerLayout drawer;
	// 初始化版本号
	@ViewById
	public TextView version;
	// 初始化开发者
	@ViewById
	public TextView developer;
	// 初始化群
	@ViewById
	public TextView group;
	// 初始化源码
	@ViewById
	public TextView github;
	
	/**
	 * 显示信息
	 *
	 * @param pMsg
	 */
	protected void ShowToast(String pMsg)
	{
		Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 打开Activity
	 *
	 * @param pClass
	 */
	protected void OpenActivity(Class<?> pClass)
	{
		startActivity(new Intent(this, pClass));
	}
	
	/**
	 * 设置界面
	 */
	public void initializeview()
	{
		setImmerseLayout(toolbar);
		setSupportActionBar(toolbar);
		
		// 打开 up button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// 当做 drawer toggle 放入 toolbar
		ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
		drawerToggle.syncState();
		drawer.setDrawerListener(drawerToggle);
		initialization();
	}
	
	/**
	 * 后台初始化
	 */
	@Background
	public void initialization()
	{
		// 设置关于页的版本号
		version.setText(getVersion());
		
		// 显示文字并设置链接
		// 创建一个 SpannableString对象
		SpannableString qq = new SpannableString("作者QQ：951203598");
		// 设置超链接
		qq.setSpan(new URLSpan("http://qm.qq.com/cgi-bin/qm/qr?k=jzWp1dTM-vhFloHOeO6-6kyLAovJsfSA"), 5, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// SpannableString对象设置给TextView
		developer.setText(qq);
		// 设置TextView可点击
		developer.setMovementMethod(LinkMovementMethod.getInstance());
		
		// 显示文字并设置链接
		// 创建一个 SpannableString对象
		SpannableString q = new SpannableString("QQ反馈群：247708078");
		// 设置超链接
		q.setSpan(new URLSpan("http://qm.qq.com/cgi-bin/qm/qr?k=Oi8I9tFyxfvyOugCzIAfLotzC0GZBTIf"), 6, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// SpannableString对象设置给TextView
		group.setText(q);
		// 设置TextView可点击
		group.setMovementMethod(LinkMovementMethod.getInstance());
		
		// 显示文字并设置链接
		// 创建一个 SpannableString对象
		SpannableString git = new SpannableString("获得源码：Music");
		// 设置超链接
		git.setSpan(new URLSpan("https://github.com/18312847646/Music.git"), 5, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// SpannableString对象设置给TextView
		github.setText(git);
		// 设置TextView可点击
		github.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	// 获得版本号
	private String getVersion()
	{
		try
		{
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return this.getString(R.string.version) + version;
		} catch (Exception e)
		{
			e.printStackTrace();
			return this.getString(R.string.version);
		}
	}
	
	protected void setImmerseLayout(View view)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			Window window = getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			
			int statusBarHeight = getStatusBarHeight(this.getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);
		}
	}
	
	public static int getStatusBarHeight(Context context)
	{
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0)
		{
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
