package com.example.verificadorrut

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.verificadorrut.ui.theme.VerificadorRutTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RutValidatorScreen()
        }
    }
}
fun calcularDV(rut: String): String {
    var suma = 0
    var multiplicador = 2

    // Recorremos de derecha a izquierda
    for (i in rut.reversed()) {
        suma += Character.getNumericValue(i) * multiplicador
        multiplicador++
        if (multiplicador > 7) multiplicador = 2
    }

    val resto = 11 - (suma % 11)
    return when (resto) {
        11 -> "0"
        10 -> "K"
        else -> resto.toString()
    }
}
@Composable
fun RutValidatorScreen() {
    var rut by remember { mutableStateOf("") }
    var dv by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Validador de RUT Chileno",
            style = MaterialTheme.typography.titleLarge
        )

        // Campo para el número de RUT
        OutlinedTextField(
            value = rut,
            onValueChange = { rut = it.filter { c -> c.isDigit() } }, // solo números
            label = { Text("Número de RUT (sin puntos ni guion)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Campo para el DV
        OutlinedTextField(
            value = dv,
            onValueChange = { dv = it.uppercase().take(1) }, // solo 1 caracter
            label = { Text("Dígito Verificador (DV)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (rut.isNotBlank() && dv.isNotBlank()) {
                    val dvCalculado = calcularDV(rut)
                    resultado = if (dvCalculado == dv) {
                        " RUT válido"
                    } else {
                        "X RUT inválido (DV correcto es $dvCalculado)"
                    }
                } else {
                    resultado = "X Debes ingresar RUT y DV"
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Validar RUT")
        }

        resultado?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodyLarge,
                color = if (it.startsWith("Check")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}

