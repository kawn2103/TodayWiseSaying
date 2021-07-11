package com.example.todaywisesaying

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todaywisesaying.databinding.ItemQuoteBinding

class QuotesPagerAdapter(
    private val quotes: List<Quote>,
    private val isNameRevealed: Boolean
): RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quote,parent,false)
        )

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quotes.size


        holder.bind(quotes[actualPosition],isNameRevealed)
    }

    override fun getItemCount() = Int.MAX_VALUE

    inner class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


        private val quoteTV = itemView.findViewById<TextView>(R.id.quoteTV)
        private val nameTV = itemView.findViewById<TextView>(R.id.nameTV)
        fun bind(quote:Quote, isNameRevealed: Boolean){
            quoteTV.text = "\"${quote.quote}\""

            if (isNameRevealed){
                nameTV.text = "- ${quote.name}"
                nameTV.visibility = View.VISIBLE
            }else{
                nameTV.visibility = View.GONE
            }

        }
    }
}