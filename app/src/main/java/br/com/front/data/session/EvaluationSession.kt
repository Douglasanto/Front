package br.com.front.data.session

import br.com.front.screens.app.avaliacoes.model.Evaluation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

object EvaluationSession {
    private val _evaluations = MutableStateFlow<List<Evaluation>>(emptyList())
    val evaluations: StateFlow<List<Evaluation>> = _evaluations

    fun addEvaluation(evaluation: Evaluation) {
        _evaluations.value = _evaluations.value + evaluation
    }

    fun clear() {
        _evaluations.value = emptyList()
    }
}
