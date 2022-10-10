package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivitySafetyTipsBinding;

public class SafetyTipsActivity extends AppCompatActivity {

    private ActivitySafetyTipsBinding binding;
    private String[] questions;
    private int position = 0;
    private int sequence = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySafetyTipsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questions = getResources().getStringArray(R.array.questions);
        String question = questions[position].split(";")[0];
        binding.textView2.setText(question);
        binding.quizSequence.setText("Quiz "+ sequence);

        binding.backButton2.setOnClickListener(view -> {
            finish();
        });

        binding.button2.setOnClickListener(view ->{
            buttonOnClick(view,true);
        });

        binding.button3.setOnClickListener(view ->{
            buttonOnClick(view,false);
        });

        binding.button4.setOnClickListener(view ->{
            position = -1;
            buttonOnClick(view,true);
        });

        binding.confirmButton.setOnClickListener(view ->{
            if (binding.postcodeEditText.getText().toString().isEmpty()){
                binding.postcodeEditText.setError("Postcode cannot be empty!");
                return;
            }
            String postcode = binding.postcodeEditText.getText().toString();
            Intent intent = new Intent(this,SafetyReportActivity.class);
            intent.putExtra("postcode",postcode);
            startActivity(intent);
            finish();
        });
    }

    private void buttonOnClick(View view,boolean flag){
        Button button = (Button)view;
        button.setBackgroundResource(R.drawable.button_shape2);
        button.setTextColor(getResources().getColor(R.color.white));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.button2.setBackgroundResource(R.drawable.button_shape);
                binding.button2.setTextColor(getResources().getColor(R.color.safetyTip));
                binding.button3.setBackgroundResource(R.drawable.button_shape);
                binding.button3.setTextColor(getResources().getColor(R.color.safetyTip));
                binding.button4.setBackgroundResource(R.drawable.button_shape);
                binding.button4.setTextColor(getResources().getColor(R.color.safetyTip));
                setNextQuestion(flag);
            }
        },500);
    }

    private void setNextQuestion(boolean answer){
        if(position == -1){
            showEditPostcode();
            return;
        }
        String[] currentQa = questions[position].split(";");
        int nextPosition = answer?Integer.parseInt(currentQa[1]):Integer.parseInt(currentQa[2]);
        sequence += 1;
        binding.quizSequence.setText("Quiz "+ sequence);
        if(nextPosition == -1){
            showEditPostcode();
            return;
        }
        if(nextPosition == -2){
            finish();
            return;
        }
        String[] nextQa = questions[nextPosition].split(";");
        String question = nextQa[0];
        binding.textView2.setText(question);
        position = nextPosition;
    }

    private void showEditPostcode(){
        binding.button2.setVisibility(View.INVISIBLE);
        binding.button3.setVisibility(View.INVISIBLE);
        binding.button4.setVisibility(View.INVISIBLE);
        binding.textView2.setText("Where do you usually go alone?");
        binding.confirmButton.setVisibility(View.VISIBLE);
        binding.postcodeEditText.setVisibility(View.VISIBLE);
        binding.textView22.setVisibility(View.VISIBLE);
    }
}