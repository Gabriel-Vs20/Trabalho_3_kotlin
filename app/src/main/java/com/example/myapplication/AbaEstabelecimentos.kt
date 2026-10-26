package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AbaEstabelecimentos(navController: NavHostController, viewModel: MeuViewModel) {

    var nome by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var formularioAberto by remember { mutableStateOf(false) }

    // busca os estabelecimentos (e os agendamentos, para a contagem de cada card) no banco
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
                        TituloSecao("Estabelecimentos")
                        Text(
                            "${viewModel.estabelecimentos.size} cadastrados",
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
                        Text("Novo estabelecimento", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1F2937))
                        Spacer(modifier = Modifier.padding(5.dp))

                        OutlinedTextField(
                            value = nome,
                            onValueChange = { nome = it },
                            label = { Text("Nome") },
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

                        OutlinedTextField(
                            value = categoria,
                            onValueChange = { categoria = it },
                            label = { Text("Categoria") },
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

                        OutlinedTextField(
                            value = nota,
                            onValueChange = { nota = it },
                            label = { Text("Nota") },
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

                        OutlinedTextField(
                            value = endereco,
                            onValueChange = { endereco = it },
                            label = { Text("Endereço") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF009739),
                                focusedLabelColor = Color(0xFF009739),
                                cursorColor = Color(0xFF009739)
                            )
                        )

                        if (viewModel.erroEstabelecimento.isNotBlank()) {
                            Spacer(modifier = Modifier.padding(4.dp))
                            CaixaErro(viewModel.erroEstabelecimento)
                        }

                        Spacer(modifier = Modifier.padding(6.dp))

                        Button(
                            onClick = {
                                if (viewModel.adicionarEstabelecimento(nome, categoria, nota, endereco)) {
                                    viewModel.adicionarNotificacao("Novo estabelecimento", "$nome foi adicionado à sua lista.")
                                    nome = ""
                                    categoria = ""
                                    nota = ""
                                    endereco = ""
                                    formularioAberto = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009739)),
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) { Text("Adicionar", fontWeight = FontWeight.ExtraBold) }
                    }
                }
            }

            items(viewModel.estabelecimentos, key = { it.id }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Rotas.detalheEstabelecimento(item.id)) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp).background(Color(0xFF009739), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.padding(6.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.nome, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1F2937))
                            Text(item.categoria, fontSize = 12.sp, color = Color(0xFF6B7A74))
                            Spacer(modifier = Modifier.padding(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(" ${item.nota}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7A74))
                                Text("  ${viewModel.agendamentosDoEstabelecimento(item.id).size} agendamentos", fontSize = 12.sp, color = Color(0xFF6B7A74))
                            }
                        }

                        IconButton(onClick = { viewModel.removerEstabelecimento(item) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Remover",
                                tint = Color(0xFFC62828)
                            )
                        }
                    }
                }
            }

            if (viewModel.estabelecimentos.isEmpty()) {
                item {
                    CaixaVazia("Nenhum estabelecimento na lista. Toque em Novo para adicionar.")
                }
            }

        }
    }

}