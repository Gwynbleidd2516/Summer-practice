package src.main.Algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;

import src.main.Edge;
import src.main.Node;

public class TopologicalSortStack extends TopologicalSort {
    private enum Statement {
        WHITE,
        GRAY,
        BLACK
    }

    private ArrayList<Boolean> mVisited;
    private ArrayList<Statement> mColors;
    private Stack<Integer> mStack;
    private Stack<Integer> mResultStack;
    private Integer mCurrentNode;

    public TopologicalSortStack(ArrayList<Node> nodes, ArrayList<Edge> edges) {
        super(nodes, edges);
        mVisited = new ArrayList<>(mNodes.size());
        mColors = new ArrayList<>(mNodes.size());
        mStack = new Stack<>();
        mResultStack = new Stack<>();
        mCurrentNode = 0;
    }

    @Override
    public void init() {
        for (int i = 0; i < mNodes.size(); i++) {
            mVisited.add(false);
            mColors.add(Statement.WHITE);
            mNodes.get(i).mColor = Color.WHITE;
        }
    }

    @Override
    public void stepForward() {
        boolean allBlack = true;
        for (Statement st : mColors) {
            if (st != Statement.BLACK) {
                allBlack = false;
                break;
            }
        }
        if (allBlack) {
            return;
        }

        if (mStack.isEmpty()) {
            int start = -1;
            for (int i = 0; i < mColors.size(); i++) {
                if (mColors.get(i) == Statement.WHITE) {
                    start = i;
                    break;
                }
            }
            if (start == -1) {
                return;
            }
            mStack.push(start);
            mColors.set(start, Statement.GRAY);
            mVisited.set(start, true);
            mNodes.get(start).mColor = Color.GRAY;
            mCurrentNode = start;
        } else {
            int v = mStack.peek();
            int next = -1;
            for (Edge e : mEdges) {
                if (e.u == v && mColors.get(e.v) == Statement.WHITE) {
                    next = e.v;
                    break;
                }
            }
            if (next != -1) {
                mStack.push(next);
                mColors.set(next, Statement.GRAY);
                mVisited.set(next, true);
                mNodes.get(next).mColor = Color.GRAY;
                mCurrentNode = next;
            } else {
                mColors.set(v, Statement.BLACK);
                mNodes.get(v).mColor = Color.BLACK;
                mStack.pop();
                mResultStack.push(v);
                mCurrentNode = mStack.isEmpty() ? null : mStack.peek();

                if (mStack.isEmpty()) {
                    boolean allBlackNow = true;
                    for (Statement st : mColors) {
                        if (st != Statement.BLACK) {
                            allBlackNow = false;
                            break;
                        }
                    }
                    if (allBlackNow) {
                        while (!mResultStack.isEmpty()) {
                            mResult.add(mResultStack.pop());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void stepBack() {
        if (mStack.isEmpty()) {
            if (!mResultStack.isEmpty()) {
                int v = mResultStack.pop();
                mColors.set(v, Statement.GRAY);
                mNodes.get(v).mColor = Color.GRAY;
                mStack.push(v);
                mCurrentNode = v;
                return;
            }

            if (!mResult.isEmpty()) {
                ArrayList<Integer> temp = new ArrayList<>(mResult);
                mResult.clear();

                int v = temp.remove(0);
                mColors.set(v, Statement.GRAY);
                mNodes.get(v).mColor = Color.GRAY;
                mStack.push(v);
                mCurrentNode = v;

                mResultStack.clear();
                for (int i = temp.size() - 1; i >= 0; i--) {
                    mResultStack.push(temp.get(i));
                }
                return;
            }
            return;
        }

        int topStack = mStack.peek();

        if (!mResultStack.isEmpty()) {
            int topResult = mResultStack.peek();

            boolean hasEdge = false;
            for (Edge e : mEdges) {
                if (e.u == topStack && e.v == topResult) {
                    hasEdge = true;
                    break;
                }
            }

            if (hasEdge) {
                int v = mResultStack.pop();
                mColors.set(v, Statement.GRAY);
                mNodes.get(v).mColor = Color.GRAY;
                mStack.push(v);
                mCurrentNode = v;
            } else {
                int v = mStack.pop();
                mColors.set(v, Statement.WHITE);
                mNodes.get(v).mColor = Color.WHITE;
                mVisited.set(v, false);
                mCurrentNode = mStack.isEmpty() ? null : mStack.peek();
            }
        } else {
            int v = mStack.pop();
            mColors.set(v, Statement.WHITE);
            mNodes.get(v).mColor = Color.WHITE;
            mVisited.set(v, false);
            mCurrentNode = mStack.isEmpty() ? null : mStack.peek();
        }
    }

    @Override
    public void reset() {
        mVisited.clear();
        mStack.clear();
        mResultStack.clear();
    }

    @Override
    public Boolean isEnd() {
        for (Statement st : mColors) {
            if (st != Statement.BLACK) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean isBegin() {
        if (!mStack.isEmpty()) {
            return false;
        }
        for (Statement st : mColors) {
            if (st != Statement.WHITE) {
                return false;
            }
        }
        return true;
    }

}
