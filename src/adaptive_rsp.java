/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OOP_TeamProject.TeamWinners;
import OOP_TeamProject.RSP.RSPPlayable;
import static OOP_TeamProject.RSP.RSPPlayable.Paper;
import static OOP_TeamProject.RSP.RSPPlayable.ROCK;
import static OOP_TeamProject.RSP.RSPPlayable.Scissors;
import OOP_TeamProject.RSP.RSPTester;
import java.util.Random;

/**
 *
 * @author PSJ
 */
public class TeamWinnersRSP2 implements RSPPlayable{
    
    int totalrun= RSPTester.HowManyRuns;
    private String GroupName = "TeamWinners";
    private int PreviousOpponentNumber, PreviousOwnNumber;
    private int index = 0;
    private int NextUserMove;
    static float[][] MatrixForClass;
    private float FirstPercentage=0.25f, SecondPercentage=0.50f, ThirdPercentage=0.75f;
    private int RockCount = 0, ScissorCount = 0, PaperCount = 0;
    private int[] opp = new int[totalrun];
    private int[] user = new int[totalrun];
    public float row0min=0, row1min=0, row2min=0, column0max=0, column1max=0, column2max=0; 
    public int row0min_index=0, row1min_index=0, row2min_index=0, column0max_index=0, column1max_index=0, column2max_index=0;
    public static float AverageOfStrategy1 = 0, AverageOfStrategy2 = 0, AverageOfStrategy3 = 0, AverageOfStrategy4 = 0;
            
    public void setGain(float[][] gainMatrix) {
        MatrixForClass = gainMatrix;
        
        int i = 0;
        for(i=0;i<3;i++){
            if(gainMatrix[i][0]<=row0min){
                row0min=gainMatrix[i][0];
                row0min_index=i;
            } 
        }
        
        for(i=0;i<3;i++){
            if(gainMatrix[i][1]<=row1min){
                row1min=gainMatrix[i][1];
                row1min_index=i;
            } 
        }

        for(i=0;i<3;i++){
            if(gainMatrix[i][2]<=row2min){
                row2min=gainMatrix[i][2];
                row2min_index=i;
            } 
        }
        
        
        for(i=0;i<3;i++){
            if(gainMatrix[0][i]>=column0max){
                column0max=gainMatrix[0][i];
                column0max_index=i;
            } 
        }
        
        for(i=0;i<3;i++){
            if(gainMatrix[1][i]>=column1max){
                column1max=gainMatrix[1][i];
                column1max_index=i;
            } 
        }
        
        for(i=0;i<3;i++){
            if(gainMatrix[2][i]>=column2max){
                column2max=gainMatrix[2][i];
                column2max_index=i;
            } 
        }
    }
    
    public void rememberOpponentMove(int oppmove, int yourmove) {
        this.PreviousOpponentNumber = oppmove;
        this.PreviousOwnNumber = yourmove;
        opp[index] = oppmove;
        user[index] = yourmove;
        
        index++;
    }
    
    public String getYourGroupName() {
        
        return GroupName;
    }
    
    public int getUserMove(int playermode) { //Rock = 0, Scissors = 1. Paper = 2
        float RandomNumberForStrategy = (float) Math.random();
        int opponentmode;
        float SumOfStrategy1=0, SumOfStrategy2=0, SumOfStrategy3=0, SumOfStrategy4=0;
        float CountOfStrategy1=0, CountOfStrategy2=0, CountOfStrategy3=0, CountOfStrategy4=0;
        float ScoreOfThistime = RSPTester.AdvancetempGain; 
        
        if (playermode == 0)
            opponentmode = 1;
        else if (playermode == 1)
            opponentmode = 0;
        
        
        if (totalrun%500 == 0) { //500번마다 확률이 조정될 수 있도록. %500d을 한 나머지가 0이 될 때 함수 실행
            MovementOfPercentage();
        }
        
        if (index >= 100) { //전략 선택을 위해서 랜덤한 숫자가 선택될 수 잇도록 위에서 정한
            if (RandomNumberForStrategy < FirstPercentage) { //랜덤 전략이 전략1번의 구간 내에 들어오면
                int NextUserMove = MinimumDamage(MatrixForClass,playermode); 
                CountOfStrategy1++; //전략1번 시행총횟수 증가(=이익최대화전략)
                SumOfStrategy1 += ScoreOfThistime; //이번턴의 점수를 전략1번의 점수에 더해줌(전략1번의 총점 저장)
                AverageOfStrategy1 = SumOfStrategy1/CountOfStrategy1;//전략1번의 평균 저장
            }//첫번째 퍼센트가 랜덤 전략보다 작거나 작고, 랜점 전략은 두번째 퍼센트보다 작으면
            else if (FirstPercentage <= RandomNumberForStrategy && RandomNumberForStrategy < SecondPercentage) {
                int NextUserMove = maxBenefit(playermode);//다음 전략은 최대화전략
                CountOfStrategy2++; //전략2번 총횟수 증가
                SumOfStrategy2 += ScoreOfThistime; //전략2의 총점 저장
                AverageOfStrategy2 = SumOfStrategy2/CountOfStrategy2; //전략2의 평균 저장
            }
            else if (SecondPercentage <= RandomNumberForStrategy && RandomNumberForStrategy< ThirdPercentage) {
                int NextUserMove = Markov(playermode, opp[index - 3], opp[index - 2], opp[index - 1]);
                CountOfStrategy3++;
                SumOfStrategy3 += ScoreOfThistime;
                AverageOfStrategy3 = SumOfStrategy3/CountOfStrategy3;
            }
            else if (ThirdPercentage < RandomNumberForStrategy) {
                int NextUserMove = RandomNumberForRSP(playermode);
                
                CountOfStrategy4++;
                SumOfStrategy4 += ScoreOfThistime;
                AverageOfStrategy4 = SumOfStrategy4/CountOfStrategy4;
            }
        }
        else { //초기에는 마코프 체인이 실행되면 안되기 때문에 index가 100보다 작은 경우에 전략3번은 제외한 내용 구현
            if (RandomNumberForStrategy < 0.33f) { 3개의 전략을 처음에 공평한 내용으로 가져갈 수 있도록
                int NextUserMove = MinimumDamage(MatrixForClass,playermode); //손실최소화 전략 선택
                
                CountOfStrategy1++; //1번 증가
                SumOfStrategy1 += ScoreOfThistime;
                AverageOfStrategy1 = SumOfStrategy1/CountOfStrategy1;
            }
            else if (0.33 <= RandomNumberForStrategy && RandomNumberForStrategy < 0.67) {
                int NextUserMove = maxBenefit(playermode);
                
                CountOfStrategy2++;
                SumOfStrategy2 += ScoreOfThistime;
                AverageOfStrategy2 = SumOfStrategy2/CountOfStrategy2;
            }
            else if (0.67 <= RandomNumberForStrategy && RandomNumberForStrategy < 1.0) {
                int NextUserMove = RandomNumberForRSP(playermode);
                
                CountOfStrategy4++;
                SumOfStrategy4 += ScoreOfThistime;
                AverageOfStrategy4 = SumOfStrategy4/CountOfStrategy4;
            }
        }
        return NextUserMove;
    }
    
    public int MinimumDamage(float[][] gainMatrix, int playermode) {
        float Column_Element1 = 0, Column_Element2 = 0, Column_Element3 = 0;
        float Row_Element1 = 0, Row_Element2 = 0, Row_Element3 = 0;
        int FirstUserMove = 0, SecondUserMove = 0;
        float FirstTotal = 0, SecondTotal = 0;
        int FinalNumber = 0;
        if (playermode == 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (j == 0) {
                        Column_Element1 = gainMatrix[i][j];
                    } else if (j == 1) {
                        Column_Element2 = gainMatrix[i][j];
                    } else if (j == 2) {
                        Column_Element3 = gainMatrix[i][j];
                    }
                }

                if ((Column_Element3 * Column_Element2 * Column_Element1 < 0) || ((Column_Element1 > 0) && (Column_Element2 > 0) && (Column_Element3 > 0))) {
                    if (Column_Element3 * Column_Element2 * Column_Element1 < 0) {
                        FirstTotal = Column_Element3 + Column_Element2 + Column_Element1;
                        FirstUserMove = i;
                    } else if (((Column_Element1 > 0) && (Column_Element2 > 0) && (Column_Element3 > 0))) {
                        SecondTotal = Column_Element3 + Column_Element2 + Column_Element1;
                        SecondUserMove = i;
                    }

                }
            }

        } else if (playermode == 1) {
            for (int j = 0; j < 3; j++) {
                for (int i = 0; j < 3; j++) {
                    if (i == 0) {
                        Row_Element1 = gainMatrix[i][j];
                    } else if (i == 1) {
                        Row_Element2 = gainMatrix[i][j];
                    } else if (i == 2) {
                        Row_Element3 = gainMatrix[i][j];
                    }
                }
                if ((Row_Element1 < 0 && Row_Element2 < 0 && Row_Element3 < 0) || (Column_Element3 * Column_Element2 * Column_Element1 > 0)) {
                    if (Row_Element1 < 0 && Row_Element2 < 0 && Row_Element3 < 0) {
                        FirstTotal = Column_Element3 + Column_Element2 + Column_Element1;
                        FirstUserMove = j;
                    } else if (Column_Element3 * Column_Element2 * Column_Element1 > 0) {
                        SecondTotal = Column_Element3 + Column_Element2 + Column_Element1;
                        SecondUserMove = j;
                    }
                }
            }
        }
        if (playermode == 0) {
            if (FirstTotal > SecondTotal) {
                FinalNumber = FirstUserMove;
            } else {
                FinalNumber = SecondUserMove;
            }
        } else if (playermode == 1) {
            if (FirstTotal < SecondTotal) {
                FinalNumber = FirstUserMove;
            } else {
                FinalNumber = SecondUserMove;
            }
        }
        return FinalNumber;  
    }
    public int maxBenefit(int playermode) { //이득을 최대로 하자 = 절댓값이 큰 성분을 찾게 됨
        float tempMax = 0;  //tempMax의 초기값을 0으로 설정
        int maxIndexRow = 0;  //내가 row-side에 있을 때 내가 내게 될 move의 index
        int maxIndexColumn = 0;   //내가 column-side에 있을 때 내가 내게 될 move의 index
        if (playermode == 0) {  //내가 row-side일 때는 절댓값이 가장 큰 양수 성분을 찾게 됨(가장 큰 수)
            for (int i = 0; i < MatrixForClass.length; i++) {
                for (int j = 0; j < MatrixForClass.length; j++) { //행렬의 원소를 모두 비교해서 가장 큰 점수 찾음(양수)
                    if (tempMax < MatrixForClass[i][j]) {         //가장 큰 점수를 찾기 때문에 처음의 기준을 0으로 설정해도 됨
                        tempMax = MatrixForClass[i][j];           //더 큰 원소 있으면 tempMax 계속 업데이트
                        maxIndexRow = i;                //더 작은 원소 나올 때마다 그 원소가 포함된 row의 index 저장
                    }
                }
            }
            return maxIndexRow; //가장 큰 성분이 포함된 row의 index 반환
        } else {  //내가 column-side일 때는 절댓값이 가장 큰 음수 성분을 찾게 됨(가장 작은 수)
            for (int i = 0; i < MatrixForClass.length; i++) {     //column-side에서는 모든 성분의 부호가 반대
                for (int j = 0; j < MatrixForClass.length; j++) {   //행렬의 원소를 모두 비교해서 가장 작은 점수 찾음(음수)
                    if (tempMax > MatrixForClass[i][j]) {        //가장 작은 점수를 찾기 때문에 처음의 기준을 0으로 설정해도 됨
                        tempMax = MatrixForClass[i][j];       //작은 원소 있으면 tempMax 계속 업데이트
                        maxIndexColumn = j;         //작은 원소 나올 때마다 그 원소가 포함된 column의 index 저장
                    }
                }
            }
            return maxIndexColumn;  //가장 작은 성분이 포함된 column의 index 반환
        }
    }
    
    /*
    public int minDamage(int playmode) {
        //최대 이익을 추구할 때와 달리 절댓값이 가장 작은 수를 찾게 되는 것인가...
    }
    */
    
    //     public int Markov(int oppmove, int oppmove1, int oppmove2,int oppmove3, int oppmove4, int oppmove5) { //6차
    public int Markov(int playermode, int oppmove, int oppmove1, int oppmove2) {
        int decision = 0;
        
        //6차 마코프 사용할거라서 현재부터 이전 6개의 상대방 move 수 저장 -->원래 6차 하려했는데 6차 하니까 지는 경우가 더 많아서 일단 없앰 그리고 판수 커지면 너무 오래걸림
//        for (int i = 0; i < index - 5; i++) {
//            if (opp[i] == oppmove) {
//                if (opp[i + 1] == oppmove1 && opp[i + 2] == oppmove2 && opp[i + 3] == oppmove3 && opp[i+4] ==oppmove4 && opp[i+5] == oppmove5 && opp[i+6] == ROCK) {
//                    rock++;
//                } else if (opp[i + 1] == oppmove1 && opp[i + 2] == oppmove2 && opp[i + 3] == oppmove3 && opp[i+4] ==oppmove4 && opp[i+5] == oppmove5 && opp[i+6]== Scissors) {
//                    scissor++;
//                } else if (opp[i + 1] == oppmove1 && opp[i + 2] == oppmove2 && opp[i + 3] == oppmove3 && opp[i+4] ==oppmove4 && opp[i+5] == oppmove5 && opp[i+6]== Paper) {
//                    paper++;
//                }
//            }
//        }
        //3차 마코프 사용할거라서 현재부터 이전 3개의 상대방 move 수 저장
        for (int i = 0; i < index - 2; i++) {
            if (opp[i] == oppmove) {
                if (opp[i + 1] == oppmove1 && opp[i + 2] == oppmove2 && opp[i + 3] == ROCK) {
                    RockCount++;
                } else if (opp[i + 1] == oppmove1 && opp[i + 2] == oppmove2 && opp[i + 3] == Scissors) {
                    ScissorCount++;
                } else if (opp[i + 1] == oppmove1 && opp[i + 2] == oppmove2 && opp[i + 3] == Paper) {
                    PaperCount++;
                }
            }
        }
        //상대방꺼 분석해서 내가 낼 거 결정
        if (playermode == 1) { //내가 col 모드
            if (RockCount < PaperCount) {
                if (PaperCount < ScissorCount) { //상대방이 가위 낼 확률이 가장 높을 때
                    decision=row1min_index ;
                } else { //바위낼 확률이 가장 높을 때
                    decision = row2min_index;
                }
            } else {
                if (RockCount > ScissorCount) { //
                    decision = row0min_index;
                } else {
                    decision = row1min_index;
                }

            }
            
        }
        else if(playermode==0){ //내가 row모드
            if (RockCount < PaperCount) {
                if (PaperCount < ScissorCount) { //상대가 가위 낼 확률이 젤 높을 때 (가위보바위
                    decision=column1max_index ; //2열에서 젤 큰 값 가져가야함
                } else { //보 낼 확률 젤 높을 때 (보 가위 바위 or 보 바위 가위
                    decision = column2max_index; //3열에서 젤 큰 값으로 내가 낼 거 결정
                }
            } else { //바위가 보 보다 확률 크고  
                if (RockCount > ScissorCount) { //바위가 확률 제일 클 때 (바위 가위 보 or 바위 보 가위
                    decision = column0max_index;
                } else { //(가위 바위 보
                    decision = column1max_index; //가위 열. 2열에서 가장 큰값
                }
            }
        }
        return decision;
    }
    
    public int RandomNumberForRSP(int playermode) {
        int high = 2;
        int low = 0;
        int myRandnum = (int) (Math.random() * (high - low + 1));
        return myRandnum;
    }

    
    public void MovementOfPercentage() { 
        float RandomNumberForMovement = (float) (Math.random() * 0.3); //0~0.3까지의 랜덤한 값 저장
        if (AverageOfStrategy1 > AverageOfStrategy2 && AverageOfStrategy1 > AverageOfStrategy3 && AverageOfStrategy1 > AverageOfStrategy4) { //1번 전략이 가장 우수하면 
            if (AverageOfStrategy4 < AverageOfStrategy2 && AverageOfStrategy4 < AverageOfStrategy3) {//4번 전략이 가장 실적이 좋지 못할때
                FirstPercentage += RandomNumberForMovement;//위에서 생성한 랜덤한 조정 값을 플러스하여 1번 전략의 실행 확률을 높임
                SecondPercentage += RandomNumberForMovement;//나머지 값들은 일정하게 비율을 가질 수 있도록 동일하게 높여줌
                ThirdPercentage += RandomNumberForMovement;//그럼 마지막 전략4번의 비율은 자동적으로 올려준만큼 감소됨
            }//1번이 best &3번이 worst
            else if (AverageOfStrategy3 < AverageOfStrategy2 && AverageOfStrategy3 < AverageOfStrategy4) {
                FirstPercentage += RandomNumberForMovement;
                SecondPercentage += RandomNumberForMovement;
            }//1번이 best&2번이 worst
            else if (AverageOfStrategy2 < AverageOfStrategy3 && AverageOfStrategy2 < AverageOfStrategy4) {
                FirstPercentage += RandomNumberForMovement;
            }
        }//2번이 best
        else if (AverageOfStrategy2 > AverageOfStrategy1 && AverageOfStrategy2 > AverageOfStrategy3 && AverageOfStrategy2 > AverageOfStrategy4) {
            if (AverageOfStrategy4 < AverageOfStrategy1 && AverageOfStrategy4 < AverageOfStrategy3) {//2번이 best일 때 4번이 worst
                SecondPercentage += RandomNumberForMovement;
                ThirdPercentage += RandomNumberForMovement;
            }
            else if (AverageOfStrategy3 < AverageOfStrategy4 && AverageOfStrategy3 < AverageOfStrategy1) {
                SecondPercentage += RandomNumberForMovement;
            }
            else if (AverageOfStrategy1 < AverageOfStrategy3 && AverageOfStrategy1 < AverageOfStrategy4) {
                FirstPercentage -= RandomNumberForMovement;
            }
        }
        else if (AverageOfStrategy3 > AverageOfStrategy1 && AverageOfStrategy3 > AverageOfStrategy2 && AverageOfStrategy3 > AverageOfStrategy4) {
            if (AverageOfStrategy4 < AverageOfStrategy1 && AverageOfStrategy4 < AverageOfStrategy2) {
                ThirdPercentage += RandomNumberForMovement;
            }
            else if (AverageOfStrategy2 < AverageOfStrategy4 && AverageOfStrategy2 < AverageOfStrategy1) {
                SecondPercentage -= RandomNumberForMovement;
            }
            else if (AverageOfStrategy1 < AverageOfStrategy4 && AverageOfStrategy1 < AverageOfStrategy2) {
                FirstPercentage -= RandomNumberForMovement;
                SecondPercentage -= RandomNumberForMovement;
            }
                
        }
        else if (AverageOfStrategy4 > AverageOfStrategy1 && AverageOfStrategy4 > AverageOfStrategy2 && AverageOfStrategy4 > AverageOfStrategy3) {
            if (AverageOfStrategy3 < AverageOfStrategy2 && AverageOfStrategy3 < AverageOfStrategy1) {
                ThirdPercentage -= RandomNumberForMovement;
            }
            else if (AverageOfStrategy2 < AverageOfStrategy1 && AverageOfStrategy2< AverageOfStrategy3) {
                SecondPercentage -= RandomNumberForMovement;
                ThirdPercentage -= RandomNumberForMovement;
            }
            else if (AverageOfStrategy1 < AverageOfStrategy2 && AverageOfStrategy1 < AverageOfStrategy3) {
                FirstPercentage -= RandomNumberForMovement;
                SecondPercentage -= RandomNumberForMovement;
                ThirdPercentage -= RandomNumberForMovement;
            }
        }
    }
}