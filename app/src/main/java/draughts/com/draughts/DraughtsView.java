package draughts.com.draughts;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class DraughtsView extends View implements View.OnTouchListener {
    private Context mContext;
    private int mViewHeight;
    private int mBoardSize = 8;
    private int mHeightOfCell;
    private Paint mRedPaint = new Paint();
    private Paint mWhitePaint = new Paint();
    private Paint mBlackStroke = new Paint();
    private Paint mGreenPaint = new Paint();
    private Rect rect;
    private int[][] mBoard;
    private int mNoPiece = 0;
    private int mInValidPlace = -1;
    private int mRedPiece = 1;
    private int mWhitePiece = 2;
    private String TAG = "DraughtsView";
    private float STATUS_BAR_HEIGHT = 24;
    private float mStatusBarHeightPixels;
    private int mRedKingPiece = 3;
    private int mWhiteKingPiece = 4;
    private int mSelectedPiece = 5;
    private int mYPreviousPosition = -3;
    private int mXPreviousPosition = -3;
    private boolean isPlayerOneTurn = true;
    private boolean invalidateView = false;
    private Paint mSelectedPaint = new Paint();
    private int mMovableValue = 6;

    public DraughtsView(Context context) {
        super(context);
        init(context);
    }

    public DraughtsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DraughtsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        this.setOnTouchListener(this);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        mViewHeight = display.getWidth();
        mHeightOfCell = mViewHeight / mBoardSize;

        mBlackStroke.setColor(Color.BLACK);
        mBlackStroke.setStrokeWidth(3);
        mBlackStroke.setStyle(Paint.Style.STROKE);

        mGreenPaint.setStrokeWidth(3);
        mGreenPaint.setColor(mContext.getResources().getColor(R.color.colorGreenCell));

        mSelectedPaint.setColor(mContext.getResources().getColor(R.color.colorPrimaryDark));

        mWhitePaint.setColor(Color.WHITE);
        mRedPaint.setColor(Color.RED);
        Resources r = getResources();
        mStatusBarHeightPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, STATUS_BAR_HEIGHT, r.getDisplayMetrics());
        resetBoard();
    }

    private void resetBoard() {
        mBoard = new int[mBoardSize][mBoardSize];
        for (int i = 0; i < mBoard.length; i++) {
            for (int j = 0; j < mBoard.length; j++) {
                // Player one possible places
                if (i > mBoard.length - mBoardSize / 2) {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1))
                        mBoard[i][j] = mRedPiece;
                    else {
                        mBoard[i][j] = mInValidPlace;
                    }
                }
                // Player two possible places
                else if (i < (mBoardSize / 2 - 1)) {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1))
                        mBoard[i][j] = mWhitePiece;
                    else {
                        mBoard[i][j] = mInValidPlace;
                    }
                }
                // Either empty positions or invalid positions
                else {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                        mBoard[i][j] = mNoPiece;
                    } else {
                        mBoard[i][j] = mInValidPlace;
                    }
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        createTable(canvas);
    }

    /**
     * Handles table drawing based on board size
     *
     * @param canvas
     */
    private void createTable(Canvas canvas) {
        // Drawing rectangles alternatively to look like a table
        for (int i = 0; i < mBoardSize; i++) {
            for (int j = 0; j < mBoardSize; j++) {
                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                    rect = new Rect(mHeightOfCell * i, mHeightOfCell * j, mHeightOfCell * i + mHeightOfCell, mHeightOfCell * j + mHeightOfCell);
                    canvas.drawRect(rect, mGreenPaint);
                    canvas.drawRect(rect, mBlackStroke);
                }
            }
        }

        // Drawing pieces based on their values
        for (int x = 0; x < mBoardSize; x++) {
            for (int y = 0; y < mBoardSize; y++) {
                // Selected cell
                if (mBoard[x][y] == 5) {
                    rect = new Rect(mHeightOfCell * y, mHeightOfCell * x, mHeightOfCell * y + mHeightOfCell, mHeightOfCell * x + mHeightOfCell);
                    canvas.drawRect(rect, mSelectedPaint);
                }
                // If it is white piece
                if (mBoard[x][y] == mWhitePiece || (5 == mBoard[x][y] && mSelectedPiece == mWhitePiece)) {
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mWhitePaint);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mBlackStroke);
                }
                // If it is red piece
                else if (mBoard[x][y] == mRedPiece || (5 == mBoard[x][y] && mSelectedPiece == mRedPiece)) {
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mRedPaint);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mBlackStroke);
                }
                // If it is white king
                else if (mBoard[x][y] == mWhiteKingPiece || (5 == mBoard[x][y] && mSelectedPiece == mWhiteKingPiece)) {
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2 - mHeightOfCell / 10, mHeightOfCell / 4, mWhitePaint);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2 - mHeightOfCell / 10, mHeightOfCell / 4, mBlackStroke);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mWhitePaint);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mBlackStroke);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2 + mHeightOfCell / 10, mHeightOfCell / 4, mWhitePaint);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2 + mHeightOfCell / 10, mHeightOfCell / 4, mBlackStroke);
                }
                // If it is red king
                else if (mBoard[x][y] == mRedKingPiece || (5 == mBoard[x][y] && mSelectedPiece == mRedKingPiece)) {
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2 - mHeightOfCell / 10, mHeightOfCell / 4, mRedPaint);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2 - mHeightOfCell / 10, mHeightOfCell / 4, mBlackStroke);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mRedPaint);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mBlackStroke);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2 + mHeightOfCell / 10, mHeightOfCell / 4, mRedPaint);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2 + mHeightOfCell / 10, mHeightOfCell / 4, mBlackStroke);
                }

                // If it is movable positions
                if (mBoard[x][y] == mMovableValue) {
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 6, mSelectedPaint);
                }
            }
        }
        canvas.drawLine(0, mHeightOfCell * mBoardSize, mViewHeight, mHeightOfCell * mBoardSize, mBlackStroke);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        invalidateView = false;
        int xMaxTouchValue = mHeightOfCell * 2 * mBoardSize;
        int yMaxTouchValue = mHeightOfCell * 2 * mBoardSize;
        int xMinTouchValue = 4;
        float yminTouchValue = mStatusBarHeightPixels;
        float xTouchedValue = motionEvent.getRawX();
        float yTouchedValue = motionEvent.getRawY() - yminTouchValue;
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            if (xTouchedValue > xMinTouchValue && xTouchedValue < xMaxTouchValue && yTouchedValue > yminTouchValue
                    && yTouchedValue < yMaxTouchValue) {
            }
            int yPosition = (int) (xTouchedValue / (mHeightOfCell));
            int xPosition = (int) (yTouchedValue / (mHeightOfCell));
            Log.i(TAG, "X :: " + xPosition + " Y:: " + yPosition);
            if (xPosition >= mBoardSize || yPosition >= mBoardSize)
                return true;
            Log.i(TAG, "Value:: " + mBoard[xPosition][yPosition]);
            if (xPosition >= mBoardSize || yPosition >= mBoardSize)
                return true;
            // Highlighting the selected piece
            if (mYPreviousPosition == -3) {
                if (mBoard[xPosition][yPosition] != mWhitePiece && mBoard[xPosition][yPosition] != mRedPiece
                        && mBoard[xPosition][yPosition] != mWhiteKingPiece && mBoard[xPosition][yPosition] != mRedKingPiece) {
                    return true;
                }
                // Is red turn
                if (isPlayerOneTurn) {
                    if (mBoard[xPosition][yPosition] == mRedKingPiece || mBoard[xPosition][yPosition] == mRedPiece) {
                        setRedSelectedPosition(xPosition, yPosition);
                        getMovablePositionsForRed(xPosition, yPosition);
                        invalidateView = true;
                    }
                }
                // Is white turn
                else if (!isPlayerOneTurn) {
                    if (mBoard[xPosition][yPosition] == mWhiteKingPiece || mBoard[xPosition][yPosition] == mWhitePiece) {
                        setWhiteSelectedPosition(xPosition, yPosition);
                        invalidateView = true;
                    }
                }
            }
            // Reset the position, when piece is selected and selected the same again
            else if (mBoard[xPosition][yPosition] == 5) {
                clearSelectedPosition(xPosition, yPosition);
                resetMovablePositions();
                invalidate();
                return true;
            }
            if (invalidateView) {
                invalidate();
            }
            return true;
        }
        return true;
    }

    private void resetMovablePositions() {
        for (int i = 0; i < mBoardSize; i++) {
            for (int j = 0; j < mBoardSize; j++) {
                if (mBoard[i][j] == mMovableValue) {
                    mBoard[i][j] = mNoPiece;
                }
            }
        }
    }

    private void getMovablePositionsForRed(int xPosition, int yPosition) {
        if (mSelectedPiece == mRedKingPiece) {

        }
        getLeftMovablePositionsForRed(xPosition, yPosition, false);
        getRightMovablePositionsForRed(xPosition, yPosition, false);
    }

    private void getLeftMovablePositionsForRed(int xPosition, int yPosition, boolean isMovableMove) {
        int j = yPosition;
        for (int i = xPosition; i >= 0; i = i - 2) {
            if (i - 1 >= 0 && j - 1 >= 0) {
                // If red coin exists
                if (mBoard[i - 1][j - 1] == mRedPiece || mBoard[i - 1][j - 1] == mRedKingPiece) {
                    if (i - 2 >= 0 && j - 2 >= 0 && mBoard[i - 2][j - 2] == mNoPiece) {
                        Log.i(TAG, "Left red:: mov: x: " + (i - 2) + " y:" + (j - 2));
                        mBoard[i - 2][j - 2] = mMovableValue;
                        getRightMovablePositionsForRed(i - 2, j - 2, true);
                        getLeftMovablePositionsForRed(i - 2, j - 2, true);
                        return;
                    }
                } else if (isMovableMove || mBoard[i - 1][j - 1] == mWhitePiece || mBoard[i - 1][j - 1] == mWhiteKingPiece) {
                    return;
                } else {
                    Log.i(TAG, "Left red:: mov: x: " + (i - 1) + " y:" + (j - 1));
                    mBoard[i - 1][j - 1] = mMovableValue;
                    return;
                }
            }
            j = j - 2;
            if (j < 0 && i < 0)
                return;
        }

        /*for (int i = xPosition - 1; i >= 0; i--) {
            for (int j = yPosition - 1; j >= 0; j--) {
                Log.i(TAG, " X left:: " + i);
                // If white coin exists
                if (mBoard[i][j] == mWhitePiece) {
                    if (i - 1 >= 0 && j - 1 >= 0) {
                        // Check for empty space cross to this
                        if ((mBoard[i - 1][j - 1] == mRedPiece || mBoard[i - 1][j - 1] == mRedKingPiece)) {
                            return;
                        } else {
                            mBoard[i - 1][j - 1] = mMovableValue;
                            getRightMovablePositionsForRed(i - 1, j - 1, true);
                            getLeftMovablePositionsForRed(i - 1, j - 1, true);
                            return;
                        }
                    }
                }
                // else, red coin exists
                else if (mBoard[i][j] == mRedPiece) {
                    return;
                }
                // Empty space;
                else if (mBoard[i][j] != mMovableValue && mBoard[i][j] != mInValidPlace && !isMovableMove) {
                    Log.i(TAG, "pOSS left: X:: " + (i) + " Y:: " + (j));
                    mBoard[i][j] = mMovableValue;
                    return;
                }
                // Possible value
                else if (mBoard[i][j] == mMovableValue) {
                    return;
                }
            }
        }*/

    }

    private void getRightMovablePositionsForRed(int xPosition, int yPosition, boolean isMovableMove) {
        int j = yPosition;
        for (int i = xPosition; i >= 0; i = i - 2) {
            if (i - 1 >= 0 && j + 1 < mBoardSize) {
                // If red coin exists
                if (mBoard[i - 1][j + 1] == mRedPiece || mBoard[i - 1][j + 1] == mRedKingPiece) {
                    if (i - 2 >= 0 && j + 2 < mBoardSize && mBoard[i - 2][j + 2] == mNoPiece) {
                        Log.i(TAG, "Right red:: mov: x: " + (i - 2) + " y:" + (j + 2));
                        mBoard[i - 2][j + 2] = mMovableValue;
                        getRightMovablePositionsForRed(i - 2, j + 2, true);
                        getLeftMovablePositionsForRed(i - 2, j + 2, true);
                        return;
                    }
                } else if (isMovableMove || mBoard[i - 1][j + 1] == mWhitePiece || mBoard[i - 1][j + 1] == mWhiteKingPiece) {
                    return;
                } else {
                    Log.i(TAG, "Right red:: mov: x: " + (i - 1) + " y:" + (j + 1));
                    mBoard[i - 1][j + 1] = mMovableValue;
                    return;
                }
            }
            j = j + 2;
            if (j > mBoardSize - 1 && i < 0)
                return;
        }
    }

    /**
     * This method responsible to reassign the previous value to the position
     *
     * @param xPosition
     * @param yPosition
     */
    private void clearSelectedPosition(int xPosition, int yPosition) {
        mBoard[xPosition][yPosition] = mSelectedPiece;
        mXPreviousPosition = -3;
        mYPreviousPosition = -3;
        mSelectedPiece = -2;
    }

    /**
     * This method responsible to assign the selected piece value of white piece and white king piece
     *
     * @param xPosition
     * @param yPosition
     */
    private void setWhiteSelectedPosition(int xPosition, int yPosition) {
        mXPreviousPosition = xPosition;
        mYPreviousPosition = yPosition;
        mSelectedPiece = mBoard[xPosition][yPosition];
        mBoard[xPosition][yPosition] = 5;
    }

    /**
     * This method responsible to assign the selected piece value of red piece and red king piece
     *
     * @param xPosition
     * @param yPosition
     */
    private void setRedSelectedPosition(int xPosition, int yPosition) {
        mXPreviousPosition = xPosition;
        mYPreviousPosition = yPosition;
        mSelectedPiece = mBoard[xPosition][yPosition];
        mBoard[xPosition][yPosition] = 5;
    }

}