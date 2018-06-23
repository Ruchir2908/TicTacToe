package com.example.caatulgupta.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout rootLayout;

    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 0;
    public static final int NO_PLAYER = -1;

    public static final int INCOMPLETE = 1;
    public static final int PLAYER_X_WON = 2;
    public static final int PLAYER_O_WON = 3;
    public static final int DRAW = 4;

    public int currentStatus;

    public int currentPlayer;

    public int SIZE = 5;

    public ArrayList<LinearLayout> rows;

    public TTTButton[][] board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.rootLayout);
        // jo main layout hai usko id se dhundha

        setUpBoard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        //kaunsi file ko inflate krna hai aur kisme daalna h
        //iss file ka xml uthake ek objevt create kr dega
        //return super.onCreateOptionsMenu(menu);
        return true;//agar false return krenge to menu display nhi hoga
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//jis menu item pe click kiya hai wo mil jata hai

        int id =item.getItemId();
        if(id==R.id.resetItem){
            setUpBoard();
        }else if(id==R.id.size3){
            SIZE = 3;
            setUpBoard();

        }else if(id==R.id.size4){
            SIZE = 4;
            setUpBoard();
        }else if(id==R.id.size5){
            SIZE = 5;
            setUpBoard();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUpBoard(){
        currentStatus = INCOMPLETE;
        rows = new ArrayList<>();
        board = new TTTButton[SIZE][SIZE];
        currentPlayer = PLAYER_O;
        rootLayout.removeAllViews();//warna jab menu mein size change kr rhe the to ussi existing mein nayi rows add ho jayengi
        for(int i=0;i<SIZE;i++){

            LinearLayout linearLayout = new LinearLayout(this);//ek naya LL bnaya
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);//uski orientation set ki
            // parameters direct nhi set kr skte kyunki is obj ko ni pta ki iska parent LL h ya nhi
            // har type k layout ki properties alag hoti h
            // relative mein weight ni chlta
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            linearLayout.setLayoutParams(layoutParams);
            //layout paramss aise set kr skte h
            rootLayout.addView(linearLayout);
            //jo main layout hai usme ye chhote layout add kr rahe h
            rows.add(linearLayout);
            // rows arraylist mein bhi ye naya layout set kr rahe h
        }


        // ab har chhote layout mein naya view mtlb button add kr rhe h
        for(int i=0;i<SIZE;i++){

            for(int j=0;j<SIZE;j++){
                TTTButton button = new TTTButton(MainActivity.this);
                // apne custom button ki ek class bnayi jiski prop hum set kr skte h warna normal button mein player naam ka koi variable ni hota
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1);
                //har ek row mtlb linear layout k andar button daal rhe h
                button.setLayoutParams(layoutParams);
                button.setOnClickListener(MainActivity.this);
                // uss button k parameters set kr diye h
                LinearLayout row = rows.get(i);
                // rows arraylist se ek row uthai aur usme ye buttons add kr diye
                row.addView(button);
                board[i][j] = button;
            }
        }
    }

    @Override
    public void onClick(View view) {

        if(currentStatus == INCOMPLETE) {
            TTTButton button = (TTTButton) view;
            button.setPlayer(currentPlayer);
            checkGameStatus();
            togglePlayer();
        }
    }

    private void checkGameStatus() {


        //rows
        for(int i=0;i<SIZE;i++){
            //assume row has same player
            boolean rowSame = true;
            TTTButton firstButton = board[i][0];//row ka first button nikalana hai
            for(int j=1;j<SIZE;j++){
                TTTButton button = board[i][j];
                if(button.isEmpty() || button.getPlayer()!=firstButton.getPlayer()){
                    rowSame = false;
                    break;
                }
            }
            if(rowSame){
                int playerWon = firstButton.getPlayer();
                updateStatus(playerWon);
                return;
            }
        }

        //cols
        for(int j=0;j<SIZE;j++){
            //assume row has same player
            boolean colSame = true;
            TTTButton firstButton = board[0][j];//row ka first button nikalana hai
            for(int i=1;i<SIZE;i++){
                TTTButton button = board[i][j];
                if(button.isEmpty() || button.getPlayer()!=firstButton.getPlayer()){
                    colSame = false;
                    break;
                }
            }
            if(colSame){
                int playerWon = firstButton.getPlayer();
                updateStatus(playerWon);
                return;
            }
        }

        //first diagonal
        boolean diagonalSame = true;
        TTTButton firstButton = board[0][0];
        for(int i=0;i<SIZE;i++){
            TTTButton button = board[i][i];
            if(button.isEmpty() || button.getPlayer()!=firstButton.getPlayer()) {
                diagonalSame = false;
                break;
            }
        }
        if(diagonalSame){
            int playerWon = firstButton.getPlayer();
            updateStatus(playerWon);
            return;
        }

        //second diagonal
        diagonalSame = true;
        firstButton = board[0][SIZE-1];
        for(int i=0;i<SIZE;i++) {
            TTTButton button = board[i][SIZE - i - 1];
            if (button.isEmpty() || button.getPlayer() != firstButton.getPlayer()) {
                diagonalSame = false;
                break;
            }
        }
        if(diagonalSame){
                int playerWon = firstButton.getPlayer();
                updateStatus(playerWon);
                return;
        }

        //board full check kiya hai
        for(int i=0;i<SIZE;i++){
            for(int j=0;j<SIZE;j++){
                TTTButton button = board[i][j];
                if(button.isEmpty()){
                    return;
                }
            }
        }

        //yahan tab hi pahunchenge ki koi rows, cols, d1, d2 e nhi jeeta ya koi bhi empty ni tha
        // pura board full [par koi player  nhi jeeta abhi tk

        Toast.makeText(this,"DRAW",Toast.LENGTH_LONG).show();
        currentStatus = DRAW;
    }

    public void updateStatus(int playerWon){
        if(playerWon == PLAYER_X){
            currentStatus = PLAYER_X_WON;
            Toast.makeText(this,"PLAYER X WON",Toast.LENGTH_LONG).show();
        }else if(playerWon == PLAYER_O) {
            currentStatus = PLAYER_O_WON;
            Toast.makeText(this, "PLAYER O WON", Toast.LENGTH_LONG).show();
        }
    }

    public void togglePlayer(){
        if(currentPlayer==PLAYER_O) {
            currentPlayer = PLAYER_X;
        }else{
            currentPlayer = PLAYER_O;
        }
    }
}
