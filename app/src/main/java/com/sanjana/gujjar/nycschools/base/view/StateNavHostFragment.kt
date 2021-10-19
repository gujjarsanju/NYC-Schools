package com.sanjana.gujjar.nycschools.base.view

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import kotlinx.parcelize.Parcelize

/**
 * A subclass of [NavHostFragment] used for restoring the state of graphs in the
 * [androidx.navigation.NavController] after configuration changes.
 */
class StateNavHostFragment : NavHostFragment() {
    companion object {
        private const val KEY_GRAPH_CONFIG = "KEY_GRAPH_CONFIG"
    }

    private var graphConfig = GraphConfig()

    /**
     * Add a graph to the current [NavGraph] to this [NavHostFragment]s [androidx.navigation.NavController].
     *
     * When using this method instead of directly using the [androidx.navigation.NavController] the set graph and any
     * added graphs will be restored after configuration changes.
     *
     * @param graphInflater GraphInflater A graphInflater instance which describes the graph to be added.
     */
    fun addGraph(graphInflater: GraphInflater) {
        graphConfig = graphConfig.copy(subGraphIds = graphConfig.subGraphIds.plus(graphInflater.graphId))
        navController.graph.addAll(graphInflater.inflate(navController.navInflater))
    }

    /**
     * Set the [NavGraph] on this [NavHostFragment]s [androidx.navigation.NavController].
     *
     * When using this method instead of directly using the [androidx.navigation.NavController] the set graph and any
     * added graphs will be restored after configuration changes. This means [androidx.navigation.NavController.setGraph]
     * should not be called by the container of the [StateNavHostFragment] when restoring state.
     *
     * @param graphResId The graph res id
     * @param startDestinationArgs Destination args used for starting the start destination
     * @param block Can be used for applying changes to the [NavGraph] *before* it's set in the [androidx.navigation.NavController]
     */
    fun setGraph(@NavigationRes graphResId: Int, startDestinationArgs: Bundle? = null, block: (NavGraph) -> Unit = {}) {
        graphConfig = GraphConfig(graphResId = graphResId)
        val graph = navController.navInflater.inflate(graphResId)
        block(graph)
        navController.setGraph(graph, startDestinationArgs)
    }

    /**
     * Set the [NavGraph] start destination on this [NavHostFragment]'s [androidx.navigation.NavController].
     *
     * When using this method instead of directly using the [androidx.navigation.NavController], the start destination
     * will be restored after configuration changes.
     *
     * @param startDestinationId Start destination id
     */
    fun setStartDestination(@IdRes startDestinationId: Int) {
        graphConfig = graphConfig.copy(startDestinationId = startDestinationId)
        navController.graph.setStartDestination(startDestinationId)
        //navController.graph.startDestination = startDestinationId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.getParcelable<GraphConfig>(KEY_GRAPH_CONFIG)?.let {
            graphConfig = it
            if (graphConfig.graphResId != 0) {
                val graph = navController.navInflater.inflate(graphConfig.graphResId)
                if (graphConfig.startDestinationId != 0) {
                    //graph.startDestination = graphConfig.startDestinationId
                    graph.setStartDestination(graphConfig.startDestinationId)

                }
                graphConfig.subGraphIds.forEach { graphId ->
                    graph.addAll(navController.navInflater.inflate(graphId))
                }
                navController.graph = graph
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_GRAPH_CONFIG, graphConfig)
    }
}

@Parcelize
private data class GraphConfig(val graphResId: Int = 0, val startDestinationId: Int = 0, val subGraphIds: List<Int> = emptyList()) :
    Parcelable
