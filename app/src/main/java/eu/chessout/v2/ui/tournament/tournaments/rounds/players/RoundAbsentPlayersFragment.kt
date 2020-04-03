package eu.chessout.v2.ui.tournament.tournaments.rounds.players

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import eu.chessout.shared.Constants
import eu.chessout.v2.R
import kotlinx.android.synthetic.main.round_absent_players_fragment.*

class RoundAbsentPlayersFragment : Fragment() {

    companion object {
        fun newInstance(clubId: String, tournamentId: String, roundId: Int) =
            RoundAbsentPlayersFragment().apply {
                arguments = Bundle().apply {
                    putString("clubId", clubId)
                    putString("tournamentId", tournamentId)
                    putInt("roundId", roundId)
                }
            }
    }

    lateinit var clubId: String
    lateinit var tournamentId: String
    var roundId: Int = -1
    lateinit var mView: View
    private val viewModel: RoundAbsentPlayersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clubId = requireArguments().getString("clubId")!!
            tournamentId = requireArguments().getString("tournamentId")!!
            roundId = requireArguments().getInt("roundId")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(Constants.LOG_TAG, "Debug round: $roundId")
        mView = inflater.inflate(R.layout.round_absent_players_fragment, container, false)
        viewModel.initialize(clubId, tournamentId)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fab.setOnClickListener {
            Log.d(
                Constants.LOG_TAG,
                "Total tournament players = ${viewModel.tournamentPlayers.size}"
            )
        }
    }
}