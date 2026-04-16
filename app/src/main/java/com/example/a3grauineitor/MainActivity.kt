package com.example.a3grauineitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a3grauineitor.ui.theme._3grauineitorTheme
import kotlin.math.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _3grauineitorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CubicSolverScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CubicSolverScreen(modifier: Modifier = Modifier) {
    var aStr by remember { mutableStateOf("") }
    var bStr by remember { mutableStateOf("") }
    var cStr by remember { mutableStateOf("") }
    var dStr by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    // Função para formatar a fórmula dinamicamente
    val dynamicFormula = remember(aStr, bStr, cStr, dStr) {
        fun formatTerm(input: String, label: String, placeholder: String, isFirst: Boolean = false): String {
            val num = input.toDoubleOrNull()
            return if (num == null) {
                val prefix = if (isFirst) "" else " + "
                "$prefix$placeholder$label"
            } else {
                val sign = if (num >= 0) (if (isFirst) "" else " + ") else " - "
                val absNum = abs(num).toString().removeSuffix(".0")
                "$sign$absNum$label"
            }
        }

        val termA = formatTerm(aStr, "x³", "a", true)
        val termB = formatTerm(bStr, "x²", "b")
        val termC = formatTerm(cStr, "x", "c")
        val termD = formatTerm(dStr, "", "d")
        
        "$termA$termB$termC$termD = 0"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Equação de 3º Grau",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Fórmula dinâmica
        Text(
            text = dynamicFormula,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )

        CoefficientInput(label = "Coeficiente a", value = aStr, onValueChange = { aStr = it })
        CoefficientInput(label = "Coeficiente b", value = bStr, onValueChange = { bStr = it })
        CoefficientInput(label = "Coeficiente c", value = cStr, onValueChange = { cStr = it })
        CoefficientInput(label = "Coeficiente d", value = dStr, onValueChange = { dStr = it })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                resultText = solveCubic(aStr, bStr, cStr, dStr)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)), // Verde
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Solução", color = Color.White, fontSize = 18.sp)
        }

        if (resultText.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resultado:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(resultText, fontSize = 16.sp)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "valeu um pontinho silverio?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}

@Composable
fun CoefficientInput(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

fun solveCubic(aStr: String, bStr: String, cStr: String, dStr: String): String {
    val a = aStr.toDoubleOrNull()
    val b = bStr.toDoubleOrNull()
    val c = cStr.toDoubleOrNull()
    val d = dStr.toDoubleOrNull()

    if (a == null || b == null || c == null || d == null) {
        return "Por favor, insira valores válidos para todos os coeficientes."
    }

    if (a == 0.0) {
        return "O coeficiente 'a' não pode ser zero em uma equação do 3º grau."
    }

    val aa = b / a
    val bb = c / a
    val cc = d / a

    val p = bb - (aa * aa / 3.0)
    val q = (2.0 * aa * aa * aa / 27.0) - (aa * bb / 3.0) + cc
    val delta = (q * q / 4.0) + (p * p * p / 27.0)
    val shift = aa / 3.0

    return if (delta > 0) {
        val sqrtDelta = sqrt(delta)
        val u = cbrt(-q / 2.0 + sqrtDelta)
        val v = cbrt(-q / 2.0 - sqrtDelta)
        
        val x1 = (u + v) - shift
        val x2Real = -(u + v) / 2.0 - shift
        val x2Imag = (u - v) * sqrt(3.0) / 2.0
        
        val f = "%.4f"
        "x1 = ${f.format(x1)}\n" +
        "x2 = ${f.format(x2Real)} + ${f.format(x2Imag)}i\n" +
        "x3 = ${f.format(x2Real)} - ${f.format(x2Imag)}i"
    } else {
        val r = sqrt(-(p * p * p) / 27.0)
        val phi = acos(-q / (2.0 * r))
        val m = 2.0 * cbrt(r)
        
        val x1 = m * cos(phi / 3.0) - shift
        val x2 = m * cos((phi + 2.0 * PI) / 3.0) - shift
        val x3 = m * cos((phi + 4.0 * PI) / 3.0) - shift
        
        val f = "%.4f"
        "x1 = ${f.format(x1)}\n" +
        "x2 = ${f.format(x2)}\n" +
        "x3 = ${f.format(x3)}"
    }
}

fun cbrt(x: Double): Double {
    return if (x >= 0) x.pow(1.0 / 3.0) else -(-x).pow(1.0 / 3.0)
}
