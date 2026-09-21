package com.example.firebaseroomyarquitecturamvvm.ui.screen.drafts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firebaseroomyarquitecturamvvm.domain.model.TaskDraft

import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftsScreen(
    onNavigateBack: () -> Unit,
    onEditDraft: (Int) -> Unit,
    viewModel: DraftsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var draftIdToDelete by remember { mutableStateOf<Int?>(null) }

    if (draftIdToDelete != null) {
        AlertDialog(
            onDismissRequest = { draftIdToDelete = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este borrador?") },
            confirmButton = {
                TextButton(onClick = {
                    draftIdToDelete?.let { viewModel.deleteDraft(it) }
                    draftIdToDelete = null
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { draftIdToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Borradores Locales") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.errorMessage != null) {
                Text(text = uiState.errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
            } else if (uiState.drafts.isEmpty()) {
                Text(text = "No hay borradores.", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(uiState.drafts, key = { it.id }) { draft ->
                        DraftItem(
                            draft = draft,
                            onPublish = { viewModel.publishDraft(draft) },
                            onDelete = { draftIdToDelete = draft.id },
                            onClick = { onEditDraft(draft.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DraftItem(draft: TaskDraft, onPublish: () -> Unit, onDelete: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = draft.title, style = MaterialTheme.typography.titleMedium)
                if (draft.description.isNotBlank()) {
                    Text(text = draft.description, style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = onPublish) {
                Icon(Icons.Default.Publish, contentDescription = "Publicar")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}