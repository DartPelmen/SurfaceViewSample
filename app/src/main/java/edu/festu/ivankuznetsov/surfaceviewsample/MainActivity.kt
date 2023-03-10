package edu.festu.ivankuznetsov.surfaceviewsample

import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import edu.festu.ivankuznetsov.surfaceviewsample.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var executor = Executors.newSingleThreadExecutor()

    var x = 0
    var y = 0
    var r = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val runnable = Runnable {
            x = binding.surfaceView.width / 2
            y = binding.surfaceView.height / 2
            r = 1
            while (true) {
                val c = binding.surfaceView.holder.lockCanvas()
                val p = Paint()
                p.color = Color.argb(x, x * y, (2 + x * y) / x, y)
                c.drawCircle(x.toFloat(), y.toFloat(), r.toFloat(), p)
                r = r % 256 + 1
                binding.surfaceView.holder.unlockCanvasAndPost(c)
            }
        }

        binding.surfaceView.holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceCreated(p0: SurfaceHolder) {

                executor.execute(runnable)
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                executor.shutdown()
            }

        })

        binding.surfaceView.setOnTouchListener { _, event ->
            setCoordinates(event.x, event.y)
            super.onTouchEvent(event)
        }
    }

    private fun setCoordinates(x: Float, y: Float) {
        this.x = x.roundToInt()
        this.y = y.roundToInt()
        r = 1
    }
}
