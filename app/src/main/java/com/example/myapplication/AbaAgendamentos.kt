package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AbaAgendamentos(navController: NavHostController, viewModel: MeuViewModel) {

    var servico by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var idSelecionado by remember { mutableStateOf(0) }
    var formularioAberto by remember { mutableStateOf(false) }

    // busca os agendamentos (e os estabelecimentos, para mostrar o nome do local) no banco
    LaunchedEffect(Unit) {
        viewModel.carregarTudo()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF4F7F6)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        TituloSecao("Meus agendamentos")
                        Text(
                            "${viewModel.agendamentos.count { !it.concluido }} em aberto",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7A74)
                        )
                    }
                    Button(
                        onClick = { formularioAberto = !formularioAberto },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009739)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text(if (formularioAberto) "  Fechar" else "  Novo", fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (formularioAberto) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Text("Novo agendamento", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1F2937))
                        Spacer(modifier = Modifier.padding(4.dp))

                        Text("ONDE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF009739))
                        Spacer(modifier = Modifier.padding(2.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            viewModel.estabelecimentos.forEach { local ->
                                val escolhido = idSelecionado == local.id
                                Text(
                                    local.nome,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (escolhido) Color.White else Color(0xFF6B7A74),
                                    modifier = Modifier
                                        .background(
                                            if (escolhido) Color(0xFF009739) else Color(0xFFF4F7F6),
                                            RoundedCornerShape(20.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (escolhido) Color(0xFF009739) else Color(0xFFE2E8F0),
                                            RoundedCornerShape(20.dp)
                                        )
                                        .clickable { idSelecionado = local.id }
                                        .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.padding(5.dp))

                        OutlinedTextField(
                            value = servico,
                            onValueChange = { servico = it },
                            label = { Text("Serviço") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF009739),
                                focusedLabelColor = Color(0xFF009739),
                                cursorColor = Color(0xFF009739)
                            )
                        )
                        Spacer(modifier = Modifier.padding(4.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = data,
                                onValueChange = { data = it },
                                label = { Text("Data") },
                                placeholder = { Text("DD/MM/AAAA") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF009739),
                                    focusedLabelColor = Color(0xFF009739),
                                    cursorColor = Color(0xFF009739)
                                )
                            )
                            OutlinedTextField(
                                value = hora,
                                onValueChange = { hora = it },
                                label = { Text("Hora") },
                                placeholder = { Text("14:30") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF009739),
                                    focusedLabelColor = Color(0xFF009739),
                                    cursorColor = Color(0xFF009739)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.padding(4.dp))

                        OutlinedTextField(
                            value = preco,
                            onValueChange = { preco = it },
                            label = { Text("Preço") },
                            placeholder = { Text("0,00") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF009739),
                                focusedLabelColor = Color(0xFF009739),
                                cursorColor = Color(0xFF009739)
                            )
                        )

                        if (viewModel.erroAgendamento.isNotBlank()) {
                            Spacer(modifier = Modifier.padding(4.dp))
                            CaixaErro(viewModel.erroAgendamento)
                        }

                        Spacer(modifier = Modifier.padding(6.dp))

                        Button(
                            onClick = {
                                if (viewModel.adicionarAgendamento(idSelecionado, servico, data, hora, preco)) {
                                    viewModel.adicionarNotificacao("Agendamento criado", "$servico marcado para $data às $hora.")
                                    servico = ""
                                    data = ""
                                    hora = ""
                                    preco = ""
                                    idSelecionado = 0
                                    formularioAberto = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009739)),
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) { Text("Agendar", fontWeight = FontWeight.ExtraBold) }
                    }
                }
            }

            items(viewModel.agendamentos, key = { it.id }) { item ->
                val local = viewModel.buscarEstabelecimento(item.idEstabelecimento)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Rotas.detalheAgendamento(item.id)) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item.concluido,
                            onCheckedChange = { viewModel.alternarConcluido(item) },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF009739))
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                item.servico,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (item.concluido) Color(0xFF9AA5A1) else Color(0xFF1F2937),
                                textDecoration = if (item.concluido) TextDecoration.LineThrough else TextDecoration.None
                            )
                            Text(
                                local?.nome ?: "Local removido",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7A74)
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Etiqueta("${item.data} ${item.hora}", Color(0xFFF4F7F6), Color(0xFF6B7A74))
                                Etiqueta("R$ %.2f".format(item.preco), Color(0xFFE6F4EC), Color(0xFF00703C))
                            }
                        }

                        IconButton(onClick = { viewModel.removerAgendamento(item) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Remover",
                                tint = Color(0xFFC62828)
                            )
                        }
                    }
                }
            }

            if (viewModel.agendamentos.isEmpty()) {
                item {
                    CaixaVazia("Nenhum agendamento na lista. Toque em Novo para criar.")
                }
            }

        }
    }

}
