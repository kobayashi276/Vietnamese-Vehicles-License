package com.example.recyclerview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclerview.model.Database;
import com.example.recyclerview.model.Question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class ThiThu extends AppCompatActivity {

    private TextView question;
    private TextView questionCount;
    private TextView countdown;
    private RadioGroup rbgroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirm;
    private ImageView imgQuestion;

    private CountDownTimer countDownTimer;
    private ArrayList<Question> questionList;
    private long timeLeftInMillis;
    private int questionCounter=0;
    private int questionSize;

    private boolean answer;
    private int count = 0;
    private Question currentQuestion;
    int correctAnswer = 0;
    int[] pics = {R.drawable.b101,R.drawable.b102,R.drawable.b105,R.drawable.b106,R.drawable.b109,R.drawable.b110,R.drawable.b111,R.drawable.b112,
            R.drawable.b113,R.drawable.b114,R.drawable.b115,R.drawable.b117,R.drawable.b120,R.drawable.b121,R.drawable.b125,R.drawable.b127,R.drawable.b134,
            R.drawable.b137,R.drawable.b138,R.drawable.b139,R.drawable.b140,R.drawable.b141};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thi_thu);

        //Xử lý sự kiện Intent để lấy ID của Category
        getID();
        Intent intent = getIntent();
        int categoryID = intent.getIntExtra("idcategories",0);
        System.out.println("cate: " + categoryID);
        Database database = new Database(this);
        //Câu hỏi thuộc thể loại ID số mấy? Số lượng câu hỏi trong 1 phần = 10
        questionList = database.getQuestions(categoryID);
        questionSize = 10;

        //Sau khi lấy toàn bộ dữ liệu từ bảng Question. Hiển thị ngẫu nhiên số câu hỏi lấy từ Database
        Collections.shuffle(questionList);
        System.out.println(questionList.size());
        showNextQuestion();

        //Check xem đã tích vào radiobox chưa, nếu chưa có radiobox nào được tích thì thông báo
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answer){
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()){
                        checkAnswer();
                    }
                    else{
                        Toast.makeText(ThiThu.this, "Vui lòng chọn câu trả lời", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    showNextQuestion();
                }
            }
        });
    }

    //Khai báo và gán các dữ liệu cho layout
    private void getID(){
        question = findViewById(R.id.question);
        questionCount = findViewById(R.id.question_count);
        countdown = findViewById(R.id.countdown);
        rbgroup = findViewById(R.id.radiogroup);
        rb1 = findViewById(R.id.radiobutton1);
        rb2 = findViewById(R.id.radiobutton2);
        rb3 = findViewById(R.id.radiobutton3);
        imgQuestion = findViewById(R.id.img_question);
        buttonConfirm = findViewById(R.id.confirm_button);
    }

    //Hiển thị câu hỏi tiếp theo, đảm bảo ràng buộc yêu cầu để qua được câu hỏi tiếp theo
    private void showNextQuestion(){
        rb1.setEnabled(true);
        rb2.setEnabled(true);
        rb3.setEnabled(true);
        rbgroup.clearCheck();
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        if (questionCounter<questionSize){
            currentQuestion = questionList.get(questionCounter);
            question.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            System.out.println(currentQuestion.toString());
            //Check nếu tích đúng câu trả lời thì câu trả lời đúng sẽ tăng lên 1
            if (currentQuestion.getImgQuestion()!=0) imgQuestion.setImageResource(pics[currentQuestion.getImgQuestion()-1]);
            questionCounter++;
            questionCount.setText("Câu "+questionCounter+"/"+questionSize);
            answer = false;
            //Nếu người dùng đã làm xong câu hỏi hiện tại thì sẽ hiện ra nút để qua câu tiếp theo
            buttonConfirm.setText("Xác Nhận");
            //Khai báo 30s
            timeLeftInMillis = 30000;
            startCountDown();
        }
        //Nếu hết câu hỏi sẽ có kết quả tổng câu đúng
        else{
            showResult();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishQuestion();
                }
            },5000);

        }
    }

    //Đếm ngược 30s
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    //Reset 30s sau khi qua câu hỏi mới
    private void updateCountDownText() {
        int minutes = (int)(timeLeftInMillis/1000)/60;
        int seconds = (int)(timeLeftInMillis/1000)%60;
        String timeFommated = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        countdown.setText(timeFommated);
        if (timeLeftInMillis<10000){
            countdown.setTextColor(Color.RED);
        }else{
            countdown.setTextColor(Color.GREEN);
        }
    }

    //Check đáp án
    private void checkAnswer(){
        rb1.setEnabled(false);
        rb2.setEnabled(false);
        rb3.setEnabled(false);
        answer = true;
        //Check vị trí mà radiobutton được chọn
        RadioButton rbSelected = findViewById(rbgroup.getCheckedRadioButtonId());
        int answerSelected = rbgroup.indexOfChild(rbSelected)-1;
//        if (answerSelected == 4) answerSelected = 1;
        System.out.println(answerSelected + " " + currentQuestion.getAnswer() + " " + correctAnswer);
        //Nếu câu trả lời được chọn trùng khớp với câu trả lời đúng trong Database thì số câu đúng +1
        if (answerSelected == currentQuestion.getAnswer()){
            correctAnswer++;
        }

        showSolution();
    }

    //Hiển thị câu trả lời đúng và sai, nếu hết 30s thì sẽ tự động hiện câu trả lời đúng
    private void showSolution() {
        switch (currentQuestion.getAnswer()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                break;
        }
        if (questionCounter < questionSize){
            buttonConfirm.setText("Câu tiếp");
        }
        else{
            buttonConfirm.setText("Hoàn tất");
        }
        countDownTimer.cancel();
    }

    private void finishQuestion() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    //Thông báo hiển thị số câu đúng
    private void showResult(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Hoàn Thành");
        alert.setMessage("Bạn đã đúng "+correctAnswer+"/10 câu");
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();

        //Hiển thị thông báo
        NotificationChannel channel = new NotificationChannel("notification", "Thông báo", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        //Hiển thị thông báo có hình
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.checked);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ThiThu.this, "notification");
        builder.setSmallIcon(R.drawable.checked);
        builder.setContentTitle("Thông báo");
        builder.setContentText("Bạn đã đúng "+correctAnswer+"/10 câu");
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));
        builder.setLargeIcon(bitmap);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(ThiThu.this);
        managerCompat.notify(1,builder.build());

    }

    //Nhấn nút back để quay lại giao diện chính
    public void onBackPressed(){
        count++;
        if (count>=1){
            finishQuestion();
        }
        count=0;
    }
}