package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalheAgendamento(navController: NavHostController, viewModel: MeuViewModel, id: Int) {

    var item by remember { mutableStateOf<Agendamento?>(null) }
    var local by remember { mutableStateOf<Estabelecimento?>(null) }
    var carregando by remember { mutableStateOf(true) }
    // muda de valor depois de salvar/concluir, para o LaunchedEffect buscar de novo no banco
    var recarregar by remember { mutableStateOf(0) }

    var servico by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var observacao by remember { mutableStateOf("") }
    var salvo by remember { mutableStateOf(false) }

    // busca o agendamento direto do banco pelo id recebido na navegação
    LaunchedEffect(id, recarregar) {
        val doBanco = viewModel.buscarAgendamentoPorId(id)
        if (doBanco != null) {
            local = viewModel.buscarEstabelecimentoPorId(doBanco.idEstabelecimento)
            if (carregando) {
                servico = doBanco.servico
                data = doBanco.data
                hora = doBanco.hora
                preco = "%.2f".format(doBanco.preco)
                observacao = doBanco.observacao
            }
        }
        item = doBanco
        carregando = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalhe do agendamento",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF009739))
            )
        }
    ) { innerPadding ->

        val item = item
        val local = local
        if (item == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (carregando) "Carregando..." else "Agendamento não encontrado",
                    color = Color(0xFF6B7A74)
                )
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF4F7F6))
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .clickable {
                        if (local != null) {
                            navController.navigate(Rotas.detalheEstabelecimento(local.id))
                        }
                    }
                    .padding(16.dp),
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
                    Text("LOCAL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF009739))
                    Text(
                        local?.nome ?: "Local removido",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1F2937)
                    )
                    Text(local?.endereco ?: "", fontSize = 12.sp, color = Color(0xFF6B7A74))
                }
                if (item.concluido) {
                    Etiqueta("Concluído", Color(0xFFE6F4EC), Color(0xFF00703C))
                } else {
                    Etiqueta("Em aberto", Color(0xFFFFF4E0), Color(0xFF9A6700))
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {

                TituloSecao("Editar agendamento")
                Spacer(modifier = Modifier.padding(5.dp))

                OutlinedTextField(
                    value = servico,
                    onValueChange = { servico = it; salvo = false },
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
                        onValueChange = { data = it; salvo = false },
                        label = { Text("Data") },
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
                        onValueChange = { hora = it; salvo = false },
                        label = { Text("Hora") },
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
                    onValueChange = { preco = it; salvo = false },
                    label = { Text("Preço") },
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
                Spacer(modifier = Modifier.padding(4.dp))

                OutlinedTextField(
                    value = observacao,
                    onValueChange = { observacao = it; salvo = false },
                    label = { Text("Observação") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
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

                if (salvo) {
                    Spacer(modifier = Modifier.padding(4.dp))
                    CaixaSucesso("Agendamento atualizado")
                }

                Spacer(modifier = Modifier.padding(6.dp))

                Button(
                    onClick = {
                        viewModel.atualizarAgendamento(item, servico, data, hora, preco, observacao) {
                            salvo = true
                            recarregar++
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009739)),
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Salvar alterações", fontWeight = FontWeight.ExtraBold) }

            }

            Spacer(modifier = Modifier.padding(6.dp))

            Button(
                onClick = { viewModel.alternarConcluido(item) { recarregar++ } },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (item.concluido) Color(0xFF6B7A74) else Color(0xFF007A2E)
                ),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(
                    if (item.concluido) "  Reabrir agendamento" else "  Marcar como concluído",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))

            OutlinedButton(
                onClick = {
                    viewModel.removerAgendamento(item)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) { Text("Excluir agendamento", color = Color(0xFFC62828), fontWeight = FontWeight.Bold) }

        }
    }

}
