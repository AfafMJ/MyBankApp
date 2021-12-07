package com.afaf.mybankapp

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private var balanceAmount: Float = 0.0f
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var bankProcesses: ArrayList<String>

    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var depositButton: Button
    private lateinit var withdrawButton: Button
    private lateinit var depositInput: EditText
    private lateinit var withdrawInput: EditText
    private lateinit var balanceshow: TextView
    private lateinit var RVBank: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyBank", MODE_PRIVATE)
        bankProcesses = ArrayList()
        depositButton = findViewById(R.id.btDeposit)
        withdrawButton = findViewById(R.id.btTWithdraw)
        depositInput = findViewById(R.id.etDeposit)
        withdrawInput = findViewById(R.id.etWithdraw)
        balanceshow = findViewById(R.id.tvBalance)
        RVBank = findViewById(R.id.rvProcess)

        balanceAmount = sharedPreferences.getFloat("myBalance", 0.0F)
        balanceshow.text = "Current Balance: ${balanceAmount.toString()}"

        when (sharedPreferences.getString("color", "white")) {
            "white" -> balanceshow.setTextColor(Color.WHITE)
            "black" -> balanceshow.setTextColor(Color.BLACK)
            "red" -> balanceshow.setTextColor(Color.RED)
        }

        adapter = RecyclerViewAdapter(bankProcesses)
        RVBank.adapter = adapter
        RVBank.layoutManager = LinearLayoutManager(this)

        fun balnceWithdrawal() {
            withdrawButton.isClickable = balanceAmount > 0
        }

        fun balanceAmountColor(): Boolean {
            if (balanceAmount > 0) {
                balanceshow.setTextColor(Color.BLACK)
                with(sharedPreferences.edit()) {
                    putFloat("myBalance", balanceAmount)
                    putString("color", "black")
                }
                return true
            } else if (balanceAmount == 0F) {
                balanceshow.setTextColor(Color.WHITE)
                with(sharedPreferences.edit()) {
                    putFloat("myBalance", balanceAmount)
                    putString("color", "white")
                }
                return true
            }

            balanceshow.setTextColor(Color.RED)
            with(sharedPreferences.edit()) {
                putFloat("myBalance", balanceAmount)
                putString("color", "red")
            }
            return false
        }


        depositButton.setOnClickListener {
            var input = depositInput.text.toString()
            depositInput.setText("")
            depositInput.hint = "Amount"
            if (input == null || input.trim() == "") {
                Snackbar.make(it, "Please enter an amount", Snackbar.LENGTH_SHORT).show()
            } else {
                val bal: Float = input.toFloat()
                balanceAmount += bal
                balanceshow.text = "Current Balance: ${balanceAmount.toString()}"
                balanceAmountColor()
                bankProcesses.add("Deposit: ${bal.toInt()}")

                balnceWithdrawal()

                RVBank.scrollToPosition(bankProcesses.size - 1)
                adapter.notifyDataSetChanged()

            }
        }

        withdrawButton.setOnClickListener {
            var input = withdrawInput.text.toString()
            withdrawInput.setText("")
            withdrawInput.hint = "Amount"
            if (input == null || input.trim() == "") {
                Snackbar.make(it, "Please enter an amount", Snackbar.LENGTH_SHORT).show()
            } else {
                val bal: Float = input.toFloat()
                balanceAmount -= bal

                bankProcesses.add("Withdrawal: ${bal.toInt()}")

                if (!balanceAmountColor()) {
                    bankProcesses.add("Negative Balance Fee: 20")
                    balanceAmount -= 20
                    with(sharedPreferences.edit()) {
                        putFloat("balance", balanceAmount)

                    }
                }
                balanceshow.text = "Current Balance: ${balanceAmount.toString()}"

                balnceWithdrawal()
                with(sharedPreferences.edit()) {
                    putFloat("balance", balanceAmount)
                }
                RVBank.scrollToPosition(bankProcesses.size - 1)
                adapter.notifyDataSetChanged()

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        bankProcesses.clear()
        adapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    override fun onRestart() {
        super.onRestart()
        val ed: Editor = sharedPreferences.edit()
        ed.putFloat("myBalance", balanceAmount)
        ed.commit()
    }
    override fun onPause() {
        super.onPause()
        val ed: Editor = sharedPreferences.edit()
        ed.putFloat("balance", balanceAmount)
        when{
            balanceAmount > 0F -> ed.putString("color", "black")
            balanceAmount == 0F -> ed.putString("color", "white")
            else -> ed.putString("color", "red")
        }
        ed.commit()
    }

}