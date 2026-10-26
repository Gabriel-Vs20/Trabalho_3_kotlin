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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AbaHome(navController: NavHostController, viewModel: MeuViewModel) {

    LaunchedEffect(Unit) {
        viewModel.carregarTudo()
    }

    val favoritos = viewModel.estabelecimentos.take(3)
    val proximos = viewModel.agendamentos.filter { !it.concluido }
    val concluidos = viewModel.agendamentos.filter { it.concluido }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF4F7F6)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            if (viewModel.nome.isNotBlank()) {
                Text(
                    "Olá, ${viewModel.nome}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1F2937)
                )
                Text("O que vamos agendar hoje?", fontSize = 13.sp, color = Color(0xFF6B7A74))
                Spacer(modifier = Modifier.padding(8.dp))
            }

            TituloSecao("Estabelecimentos Favoritos")
            Spacer(modifier = Modifier.padding(5.dp))

            if (favoritos.isEmpty()) {
                CaixaVazia("Nenhum estabelecimento cadastrado ainda")
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    favoritos.forEach { item ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                .clickable { navController.navigate(Rotas.detalheEstabelecimento(item.id)) }
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(36.dp).background(Color(0xFF009739), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Home,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(item.nome, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1F2937))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(" ${item.nota}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7A74))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            TituloSecao("Meus próximos horários")
            Spacer(modifier = Modifier.padding(5.dp))

            if (proximos.isEmpty()) {
                CaixaVazia("Você não possui horários marcados")
            } else {
                proximos.forEach { item ->
                    val local = viewModel.buscarEstabelecimento(item.idEstabelecimento)
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
                        Box(
                            modifier = Modifier.size(38.dp).background(Color(0xFFE6F4EC), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color(0xFF009739),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.padding(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.servico, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1F2937))
                            Text(
                                "${local?.nome ?: "Local removido"} - ${item.data} às ${item.hora}",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7A74)
                            )
                        }
                        Text("R$ %.2f".format(item.preco), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF009739))
                    }
                }
            }

            Spacer(modifier = Modifier.padding(4.dp))

            TituloSecao("Últimos Agendamentos")
            Spacer(modifier = Modifier.padding(5.dp))

            if (concluidos.isEmpty()) {
                CaixaVazia("Nenhum atendimento concluído por enquanto")
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    concluidos.take(3).forEach { item ->
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                .clickable { navController.navigate(Rotas.detalheAgendamento(item.id)) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color(0xFF009739),
                                modifier = Modifier.size(16.dp)
                            )
                            Text("  ${item.data.take(5)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                        }
                    }
                }
            }

        }
    }

}

@Composable
fun CaixaVazia(texto: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            texto,
            color = Color(0xFF6B7A74),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(24.dp)
        )
    }
}
