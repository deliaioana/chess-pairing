package eu.chessout.v2.ui.club.joinclub

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.shared.model.Club
import eu.chessout.v2.R
import kotlinx.android.synthetic.main.dialog_join_club.*

class JoinClubDialog() : DialogFragment() {

    private lateinit var mView: View
    private lateinit var joinClubModel: JoinClubModel
    private val myListAdapter = JoinClubAdapter(arrayListOf())

    private val myObserver = Observer<List<Club>> { list ->
        list?.let {
            my_recycler_view.visibility = View.VISIBLE
            myListAdapter.updateArrayList(it)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        mView = inflater.inflate(R.layout.dialog_join_club, container, false)

        val model: JoinClubModel by viewModels()
        joinClubModel = model
        joinClubModel.liveClubs.observe(viewLifecycleOwner, myObserver)
        joinClubModel.initializeModel()

        val myRecyclerView = mView.findViewById<RecyclerView>(R.id.my_recycler_view)
        myRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = myListAdapter
        }

        return mView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setTitle("Join club")

        return dialog
    }
}