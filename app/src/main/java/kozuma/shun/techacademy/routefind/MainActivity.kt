package kozuma.shun.techacademy.routefind

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import jp.co.yahoo.android.maps.GeoPoint
import jp.co.yahoo.android.maps.MapActivity
import jp.co.yahoo.android.maps.MapView
import jp.co.yahoo.android.maps.ar.ARControllerListener
import jp.co.yahoo.android.maps.navi.NaviController
import jp.co.yahoo.android.maps.routing.RouteOverlay
import jp.co.yahoo.android.maps.ar.ARController
import android.view.WindowManager
import android.content.pm.ActivityInfo
import android.widget.Button
import jp.co.yahoo.android.maps.MyLocationOverlay





class MainActivity : MapActivity(), RouteOverlay.RouteOverlayListener, NaviController.NaviControllerListener,
    ARControllerListener {
    override fun ARControllerListenerOnPOIPick(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private lateinit var naviController: NaviController

    private lateinit var arController: ARController

    private var _overlay: MyLocationOverlay? = null

    private  var p: GeoPoint = GeoPoint(0,0)//現在地の取得


    override fun isRouteDisplayed(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    //ルート検索が正常に終了しなかった場合
    override fun errorRouteSearch(arg0: RouteOverlay, arg1: Int): Boolean {
        return false
    }


    //ルート検索が正常に終了した場合
    override fun finishRouteSearch(routeOverlay: RouteOverlay): Boolean {
        //地図を表示
        var mapView = MapView(this, "dj0zaiZpPWowWHRab050ODJyTyZzPWNvbnN1bWVyc2VjcmV0Jng9MzY-")

        //NaviControllerを作成しRouteOverlayインスタンスを設定
        naviController = NaviController(this, routeOverlay)

        //MapViewインスタンスを設定
        naviController.setMapView(mapView)

        //NaviControllerListenerを設定
        naviController.setNaviControlListener(this)

        //案内処理を開始
        naviController.start()


        //横向き固定
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //ARControllerインスタンス作成
        arController = ARController(this, this)

        //ARControllerをNaviControllerに設定
        naviController.setARController(arController)

        //案内処理を開始
        naviController.start()

        return false
    }

    //現在位置が更新された場合
    override fun onLocationChanged(arg0: NaviController): Boolean {
        //目的地までの残りの距離
        val rema_dist = naviController.getTotalDistance()

        //目的地までの残りの時間
        val rema_time = naviController.getTotalTime()

        //出発地から目的地までの距離
        val total_dist = naviController.getDistanceOfRemainder()

        //出発地から目的地までの時間
        val total_time = naviController.getTimeOfRemainder()

        //現在位置
        val location = naviController.getLocation()
        return false
    }

    //現在位置取得エラーが発生した場合
    override fun onLocationTimeOver(arg0: NaviController): Boolean {
        return false
    }

    //現在位置の精度が悪い場合
    override fun onLocationAccuracyBad(arg0: NaviController): Boolean {
        return false
    }

    //ルートから外れたと判断された場合
    override fun onRouteOut(arg0: NaviController): Boolean {
        return false
    }

    //目的地に到着した場合
    override fun onGoal(arg0: NaviController): Boolean {

        //ARの停止処理
        arController.onPause();

        //案内処理を継続しない場合は停止させる
        naviController.stop();

        //ARControllerをNaviControllerから削除
        naviController.setARController(null);

        //案内処理を継続しない場合は停止させる
        naviController.stop()
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)


        //地図を表示
        var mapView = MapView(this, "dj0zaiZpPWowWHRab050ODJyTyZzPWNvbnN1bWVyc2VjcmV0Jng9MzY-")



//        val c = mapView.mapController
//        c.setCenter(GeoPoint(35665721, 139731006)) //初期表示の地図を指定
//        c.setZoom(1)                                 //初期表示の縮尺を指定

        setContentView(mapView)


        mapView.setBuiltInZoomControls(true)
        mapView.setScalebar(true)


        /*

        //MyLocationOverlayインスタンス作成
        _overlay = MyLocationOverlay(applicationContext, mapView)

        //現在位置取得開始
        _overlay!!.enableMyLocation()

        //位置が更新されると、地図の位置も変わるよう設定
        _overlay!!.runOnFirstFix(Runnable {
            if (mapView.mapController != null) {
                //現在位置を取得
                val p = _overlay!!.myLocation
                //地図移動
                mapView.mapController.animateTo(p)
            }
        })

        //MapViewにMyLocationOverlayを追加。
        mapView.overlays.add(_overlay)

        setContentView(mapView)
        */

        /*
        //ポインタを表示
        val mid = GeoPoint(35665721, 139731006)
        setContentView(mapView)
        val pinOverlay = PinOverlay(PinOverlay.PIN_VIOLET)
        mapView.overlays.add(pinOverlay)
        pinOverlay.addPoint(mid, null)



        //ポップアップの表示
        val mid = GeoPoint(35665721, 139731006)
        setContentView(mapView)
        val pinOverlay = PinOverlay(PinOverlay.PIN_VIOLET)
        mapView.overlays.add(pinOverlay)
        val popupOverlay = object : PopupOverlay() {
            override fun onTap(item: OverlayItem?) {
                //ポップアップをタッチした際の処理
            }
        }
        mapView.overlays.add(popupOverlay)
        pinOverlay.setOnFocusChangeListener(popupOverlay)
        pinOverlay.addPoint(mid, "東京ミッドタウン", "東京ミッドタウンについて")


        //ポリラインの表示
        val tokyo = GeoPoint(35681396, 139766049)
        val mid = GeoPoint(35665721, 139731006)
        val polylineOverlay = object : PolylineOverlay(arrayOf(tokyo, mid)) {
            override fun onTap(): Boolean {
                //ラインをタッチした際の処理
                return true
            }
        }
        mapView.overlays.add(polylineOverlay)


        //ポリゴン三角形の表示
        val gp1 = GeoPoint(35663211, 139732294)
        val gp2 = GeoPoint(35662082, 139732825)
        val gp3 = GeoPoint(35662657, 139733463)
        val polygonOverlay = object : PolygonOverlay(arrayOf(gp1, gp2, gp3)) {
            override fun onTap(): Boolean {
                //ポリゴンをタッチした際の処理
                return true
            }
        }
        polygonOverlay.setFillColor(Color.argb(127, 0, 63, 255))
        polygonOverlay.setStrokeColor(Color.argb(127, 0, 63, 255))
        mapView.overlays.add(polygonOverlay)


        //円を表示する
        val mid = GeoPoint(35665721, 139731006)
        val circleOverlay = object : CircleOverlay(mid, 50, 100) {
            override fun onTap(): Boolean {
                //円をタッチした際の処理
                return true
            }
        }
        mapView.overlays.add(circleOverlay)



        //注記を表示
        val labelTouchOverlay = object : LabelTouchOverlay() {
            override fun onLabelTouch(labelInfo: LabelTouchManager.LabelInfo) {
                //注記をタッチした際の処理
                val ad = AlertDialog.Builder(this@MainActivity) //名称を表示
                ad.setMessage(labelInfo.name)
                ad.show()
            }
        }
        mapView.overlays.add(labelTouchOverlay)


        //指定したルートを表示
        //RouteOverlay作成
        val routeOverlay = RouteOverlay(this, "dj0zaiZpPWowWHRab050ODJyTyZzPWNvbnN1bWVyc2VjcmV0Jng9MzY-")

        //出発地ピンの吹き出し設定
        routeOverlay.setStartTitle("ミッドタウン")

        //目的地ピンの吹き出し設定
        routeOverlay.setGoalTitle("東京タワー")

        //出発地、目的地、移動手段を設定
        routeOverlay.setRoutePos(
            GeoPoint(35659151, 139703504),
            GeoPoint(35659007, 139705427),
            RouteOverlay.TRAFFIC_WALK
        )


        //RouteOverlayListenerの設定
        routeOverlay.setRouteOverlayListener(this)


        //検索を開始
        routeOverlay.search()

        //MapViewにRouteOverlayを追加
        mapView.overlays.add(routeOverlay)

        */

        /*

        //MyLocationOverlayインスタンス作成
        _overlay = MyLocationOverlay(applicationContext, mapView)

        //現在位置取得開始
        _overlay!!.enableMyLocation()

        //位置が更新されると、地図の位置も変わるよう設定
        _overlay!!.runOnFirstFix(Runnable {
            if (mapView.mapController != null) {
                //現在位置を取得
                p = _overlay!!.myLocation
                //地図移動
                mapView.mapController.animateTo(p)
            }
        })

        //MapViewにMyLocationOverlayを追加。
        mapView.overlays.add(_overlay)

*/


        //RouteOverlay作成
        val routeOverlay = RouteOverlay(this, "dj0zaiZpPWowWHRab050ODJyTyZzPWNvbnN1bWVyc2VjcmV0Jng9MzY-")

        //出発地ピンの吹き出し設定
        routeOverlay.setStartTitle("ミッドタウン")

        //目的地ピンの吹き出し設定
        routeOverlay.setGoalTitle("東京タワー")

        //出発地、目的地、移動手段を設定
        routeOverlay.setRoutePos(
            GeoPoint(35658826, 139705511),
            GeoPoint(35658036, 139699446),
            RouteOverlay.TRAFFIC_WALK
        )


        //RouteOverlayListenerの設定
        routeOverlay.setRouteOverlayListener(this)

        //検索を開始
        routeOverlay.search()



        //MapViewにRouteOverlayを追加
        mapView.overlays.add(routeOverlay)



    }


}





