package com.example.task_3.ui.home

import kotlin.math.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.task_3.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.buttonCalculateTask1.setOnClickListener {Calculating()}
        return root
    }

    private fun round(num: Double) = "%.2f".format(num)

    fun gaussianFunction(x: Double, mean: Double, stdDev: Double): Double {
        val coefficient = 1 / (stdDev * sqrt(2 * Math.PI))
        val exponent = -((x - mean).pow(2)) / (2 * stdDev.pow(2))
        return coefficient * exp(exponent)
    }

    fun integrateGaussian(a: Double, b: Double, n: Int, mean: Double, stdDev: Double): Double {
        var integral = 0.0
        val h = (b - a) / n
        for (i in 0 until n) {
            val x0 = a + i * h
            val x1 = a + (i + 1) * h
            integral += (gaussianFunction(x0, mean, stdDev) + gaussianFunction(x1, mean, stdDev)) / 2 * h
        }
        return integral
    }


    private fun Calculating()
    {
        val price = binding.price.text.toString().toDouble()
        val power = binding.power.text.toString().toDouble()
        val errorBefore = binding.errorBefore.text.toString().toDouble()
        val errorAfter = binding.errorAfter.text.toString().toDouble()
        val a = power - errorAfter
        val b = power + errorAfter
        val n = 1000
        val shareWithoutImbalancesBefore = integrateGaussian(a, b, n, power, errorBefore)
        val profitBefore = power * 24 * shareWithoutImbalancesBefore * price
        val fineBefore = power * 24 * (1 - shareWithoutImbalancesBefore) * price
        val shareWithoutImbalancesAfter = integrateGaussian(a, b, n, power, errorAfter)
        val profitAfter = power * 24 * shareWithoutImbalancesAfter * price
        val fineAfter = power * 24 * (1 - shareWithoutImbalancesAfter) * price
    
        var output = "Прибуток до вдосконалення: ${round(profitBefore)} тис. грн\n" +
                    "Виручка до вдосконалення: ${round(profitBefore - fineBefore)} тис. грн\n" +
                    "Штраф до вдосконалення: ${round(fineBefore)} тис. грн\n" +
                    "Прибуток після вдосконалення: ${round(profitAfter)} тис. грн\n" +
                    "Виручка після вдосконалення: ${round(profitAfter - fineAfter)} тис. грн\n" +
                    "Штраф після вдосконалення: ${round(fineAfter)} тис. грн\n"
        binding.outputTask1.text = output;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}