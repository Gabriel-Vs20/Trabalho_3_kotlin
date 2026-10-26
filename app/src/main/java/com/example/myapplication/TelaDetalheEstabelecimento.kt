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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalheEstabelecimento(navController: NavHostController, viewModel: MeuViewModel, id: Int) {

    var local by remember { mutableStateOf<Estabelecimento?>(null) }
    var agendamentos by remember { mutableStateOf<List<Agendamento>>(emptyList()) }
    var carregando by remember { mutableStateOf(true) }

    // busca o estabelecimento e os agendamentos dele direto do banco, a partir do id da rota
    LaunchedEffect(id) {
        local = viewModel.buscarEstabelecimentoPorId(id)
        agendamentos = viewModel.buscarAgendamentosDoEstabelecimento(id)
        carregando = false
    }

    val total = agendamentos.sumOf { it.preco }
    val media = if (agendamentos.isEmpty()) 0.0 else total / agendamentos.size
    val concluidos = agendamentos.count { it.concluido }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        local?.nome ?: "Estabelecimento",
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

        val local = local
        if (local == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (carregando) "Carregando..." else "Estabelecimento não encontrado",
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
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(56.dp).background(Color(0xFF009739), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(local.nome, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF1F2937))
                    Text(local.categoria, fontSize = 13.sp, color = Color(0xFF6B7A74))
                    Spacer(modifier = Modifier.padding(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(" ${local.nota}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7A74))
                    }
                }
            }

            Spacer(modifier = Modifier.padding(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF009739),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(local.endereco, fontSize = 13.sp, color = Color(0xFF1F2937))
            }

            Spacer(modifier = Modifier.padding(8.dp))

            TituloSecao("Resumo neste local")
            Spacer(modifier = Modifier.padding(5.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CaixaResumo("Agendamentos", agendamentos.size.toString(), Modifier.weight(1f))
                CaixaResumo("Concluídos", concluidos.toString(), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.padding(5.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CaixaResumo("Total gasto", "R$ %.2f".format(total), Modifier.weight(1f))
                CaixaResumo("Ticket médio", "R$ %.2f".format(media), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.padding(8.dp))

            TituloSecao("Agendamentos neste local")
            Spacer(modifier = Modifier.padding(5.dp))

            if (agendamentos.isEmpty()) {
                CaixaVazia("Nenhum agendamento aqui ainda")
            } else {
                agendamentos.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .clickable { navController.navigate(Rotas.detalheAgendamento(item.id)) }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color(0xFF009739),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.servico, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1F2937))
                            Text("${item.data} às ${item.hora}", fontSize = 12.sp, color = Color(0xFF6B7A74))
                        }
                        if (item.concluido) {
                            Etiqueta("Concluído", Color(0xFFE6F4EC), Color(0xFF00703C))
                        } else {
                            Etiqueta("Em aberto", Color(0xFFFFF4E0), Color(0xFF9A6700))
                        }
                    }
                }
            }

        }
    }

}

@Composable
fun CaixaResumo(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(titulo, fontSize = 12.sp, color = Color(0xFF6B7A74))
        Spacer(modifier = Modifier.padding(2.dp))
        Text(valor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF009739))
    }
}
