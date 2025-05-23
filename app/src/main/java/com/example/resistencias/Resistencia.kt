package com.example.resistencias

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
@Composable
fun Resistencias() {
    val coloresBanda1y2 = listOf("Negro", "Marron", "Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Violeta", "Gris", "Blanco")
    val coloresBanda3 = listOf("Negro", "Marron", "Rojo", "Naranja", "Amarillo")
    val coloresBanda4 = listOf("Dorado", "Plateado", "Blanco")

    val coloresMap = mapOf(
        "Negro" to Color.Black,
        "Marron" to Color(0xFF5E3C23),
        "Rojo" to Color.Red,
        "Naranja" to Color(0xFFFF8400),
        "Amarillo" to Color.Yellow,
        "Verde" to Color.Green,
        "Azul" to Color.Blue,
        "Violeta" to Color(0xFF673AB7),
        "Gris" to Color.Gray,
        "Blanco" to Color.White,
        "Dorado" to Color(0xFFFFD700),
        "Plateado" to Color(0xFFC0C0C0)
    )

    val valorMap = mapOf(
        "Negro" to 0,
        "Marron" to 1,
        "Rojo" to 2,
        "Naranja" to 3,
        "Amarillo" to 4,
        "Verde" to 5,
        "Azul" to 6,
        "Violeta" to 7,
        "Gris" to 8,
        "Blanco" to 9
    )

    val toleranciaMap = mapOf(
        "Dorado" to "±5%",
        "Plateado" to "±10%",
        "Blanco" to "±20%"
    )

    val coloresPorBanda = listOf(
        coloresBanda1y2,
        coloresBanda1y2,
        coloresBanda3,
        coloresBanda4
    )

    val bandasSeleccionadas = remember { mutableStateListOf("Negro", "Negro", "Negro", "Dorado") }
    var resultado by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp,
        color = Color(0xFFC0E0F8)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Seleccione el color de las bandas de izquierda a derecha",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000209)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nombres de las bandas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Banda 1", "Banda 2", "Banda 3", "Banda 4").forEach {
                    Text(it, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Selectores de color
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(4) { seleccion ->
                    ColorSelector(
                        colores = coloresPorBanda[seleccion],
                        coloresMap = coloresMap,
                        seleccion = bandasSeleccionadas[seleccion],
                        onSeleccionChange = { bandasSeleccionadas[seleccion] = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar valor debajo de cada selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(4) { seleccion ->
                    val color = bandasSeleccionadas[seleccion]
                    val texto = when (seleccion) {
                        3 -> "Tolerancia: ${toleranciaMap[color] ?: "-"}"
                        else -> "Valor: ${valorMap[color] ?: "-"}"
                    }
                    Box(modifier = Modifier.width(72.dp), contentAlignment = Alignment.Center) {
                        Text(texto, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Resultado
            if (resultado.isNotEmpty()) {
                Text(
                    text = "Resistencia total: $resultado",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000103)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }


            Button(
                onClick = {
                    val valor1 = valorMap[bandasSeleccionadas[0]] ?: 0
                    val valor2 = valorMap[bandasSeleccionadas[1]] ?: 0
                    val multiplicador = Math.pow(
                        10.0,
                        (valorMap[bandasSeleccionadas[2]] ?: 0).toDouble()
                    )
                    val tolerancia = toleranciaMap[bandasSeleccionadas[3]] ?: ""
                    val resistencia = ((valor1 * 10 + valor2) * multiplicador).toInt()
                    resultado = "$resistencia Ω $tolerancia"
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Consultar", color = Color.White)
            }
        }
    }
}

@Composable
fun ColorSelector(
    colores: List<String>,
    coloresMap: Map<String, Color>,
    seleccion: String,
    onSeleccionChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var buttonSize by remember { mutableStateOf(Size.Zero) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .width(72.dp)
                .onGloballyPositioned { coordinates ->
                    buttonSize = coordinates.size.toSize()
                },
            shape = RoundedCornerShape(0.dp)
        ){
            Text(text = seleccion, fontSize = 10.sp)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { buttonSize.width.toDp() })
        ) {
            colores.forEach { color ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(color = coloresMap[color] ?: Color.Gray)
                                    .border(1.dp, Color.DarkGray)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(color, fontSize = 12.sp)
                        }
                    },
                    onClick = {
                        onSeleccionChange(color)
                        expanded = false
                    }
                )
            }
        }
    }
}