package eu.chessout.v2.ui.tournament.tournaments.rounds.players

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import eu.chessout.shared.Constants
import eu.chessout.shared.model.Player

class RoundAddPlayersDialog(
    val clubId: String,
    val tournamentId: String,
    val players: List<Player>
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Add absent player")

        val nameArray: Array<String> = players.map { it.name }.toTypedArray()
        builder.setItems(nameArray, DialogInterface.OnClickListener { _, witch ->
            addMissingPlayer(players[witch])
        })

        builder.setNegativeButton("Cancel") { _, _ -> dismiss() }
        return builder.create()
    }

    private fun addMissingPlayer(player: Player) {
        Log.d(Constants.LOG_TAG, "Player to ad: ${player.name}")
    }
}