package draughts.com.draughts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class DraughtsView extends View {
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

        mWhitePaint.setColor(Color.WHITE);
        mRedPaint.setColor(Color.RED);
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
                if (mBoard[x][y] == mWhitePiece) {
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mWhitePaint);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mBlackStroke);
                } else if (mBoard[x][y] == mRedPiece) {
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mRedPaint);
                    canvas.drawCircle(mHeightOfCell / 2 * (y * 2) + mHeightOfCell / 2, mHeightOfCell / 2 * (x * 2) + mHeightOfCell / 2, mHeightOfCell / 4, mBlackStroke);
                }
            }
        }
        canvas.drawLine(0, mHeightOfCell * mBoardSize, mViewHeight, mHeightOfCell * mBoardSize, mBlackStroke);
    }
}
