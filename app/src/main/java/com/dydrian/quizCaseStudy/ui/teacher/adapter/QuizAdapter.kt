package com.dydrian.quizCaseStudy.ui.teacher.adapter

import android.location.GnssAntennaInfo.Listener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dydrian.quizCaseStudy.data.model.Quiz
import com.dydrian.quizCaseStudy.databinding.ItemQuizLayoutBinding

class QuizAdapter(
    private var quizzes: List<Quiz>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQuizLayoutBinding.inflate(inflater, parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = quizzes[position]
        if (holder is QuizViewHolder) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }

    fun setQuizzes(quizzes: List<Quiz>) {
        this.quizzes = quizzes
        notifyDataSetChanged()
    }

    inner class QuizViewHolder(
        private var binding: ItemQuizLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: Quiz) {
            binding.tvTitle.text = quiz.title.uppercase()
            binding.tvTimePerQuestion.text =
                "Timer per question: ${quiz.timePerQuestion.toString()}"
            binding.tvNumberOfQuestions.text =
                "Number of questions: ${quiz.questions.size}"
        }
    }
}