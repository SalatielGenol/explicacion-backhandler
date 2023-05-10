package es.genol.ejemplobackhandler

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/*
El siguiente ejemplo trata de probar el funcionamiento de la función BackHandler.

Dicha función sirve para manejar el comportamiento del botón atrás del sistema, y se
puede personalizar.

Al consultar la documentación oficial (link de abajo) se puede ver un aviso que
nos dice que aun pudiendo usar varias llamadas a esta función dentro de la
misma composición, el comportamiento que quedará asignado será el del último
componente que se haya dibujado (o redibujado).

https://developer.android.com/jetpack/compose/libraries#handling_the_system_back_button
*/

@Composable
fun BackHandlerScreen() {

    var num1 by rememberSaveable { mutableStateOf(0) }
    var num2 by rememberSaveable { mutableStateOf(0) }
    var num3 by rememberSaveable { mutableStateOf(0) }

    /*Primera llamada a la función (ref.1)*/
    BackHandler {
        num3++
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(150.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row {
            var show by rememberSaveable { mutableStateOf(true) }
            var painter by rememberSaveable { mutableStateOf(R.drawable.baseline_visibility_off_24) }

            IconButton(onClick = { show = !show }) {
                Icon(
                    painter = painterResource(id = painter),
                    contentDescription = "Visibility"
                )
            }
            if (show) {
                painter = R.drawable.baseline_visibility_off_24
                /*Paso de comportamiento a traves de una lambda (ref.2)*/
                BackHandlerTextField(value = num1.toString(), label = "ref.2") {
                    num1++
                }
            } else {
                painter = R.drawable.baseline_visibility_24
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Row {
            var show by rememberSaveable { mutableStateOf(true) }
            var painter by rememberSaveable { mutableStateOf(R.drawable.baseline_visibility_off_24) }

            IconButton(onClick = { show = !show }) {
                Icon(
                    painter = painterResource(id = painter),
                    contentDescription = "Visibility"
                )
            }

            if (show) {
                painter = R.drawable.baseline_visibility_off_24


                /*Paso de comportamiento a través de una lambda (ref.3)*/
                BackHandlerTextField(value = num2.toString(), label = "ref.3") {
                    num2++
                }
            } else {
                painter = R.drawable.baseline_visibility_24
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            value = num3.toString(),
            onValueChange = {},
            label = { Text(text = "ref.1") })
    }
}


/*
Elemento Composable que incluye una ejecución de la función Backhandler,
y que se realiza en el momento de composición por un elemento superior,
en este ejemplo se llama desde BackHandlerScreen()
*/
@Composable
private fun BackHandlerTextField(value: String, label: String, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    OutlinedTextField(value = value, onValueChange = {}, label = { Text(text = label) })
}

/*
Funcionamiento:

- Teniendo en cuenta que el comportamiento del botón atrás del dispositivo lo va a definir
el ultimo elemento que se haya dibujado, al iniciar por primera vez la aplicación, el comportamiento
asignado será el de la ref.3 ya que es el ultimo elemento que se pinta.

- Si ocultamos ese textfield, el comportamiento asignado sería el de la ref.2
- Si ocultásemos los dos textfield, el comportamiento a ejecutar pasaría a ser el principal de la
screen, el de la ref.1

- Pero si mostramos los dos textfield que previamente hemos ocultado, pero invirtiendo el orden:
es decir, primero mostramos el ref.3 y después el ref.2, el comportamiento asignado al botón atrás
del dispositivo sería el de ref.2, ya que es el último que se ha redibujado.
*/