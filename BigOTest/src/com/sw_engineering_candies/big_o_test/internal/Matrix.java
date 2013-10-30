package com.sw_engineering_candies.big_o_test.internal;

/**
 * It is based on Matrix.java form Robert Sedgewick and Kevin Wayne
 * (http://introcs.cs.princeton.edu/java/95linear/Matrix.java.html)
 * 
 */

final public class Matrix {

	final int row;

	final int col;

	final double[][] matrix;

	public Matrix(int row, int col) {
		this.row = row;
		this.col = col;
		matrix = new double[row][col];
	}

	public Matrix(Matrix A) {
		this(A.matrix);
	}

	public Matrix(double[][] matrix) {
		row = matrix.length;
		col = matrix[0].length;
		this.matrix = new double[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				this.matrix[i][j] = matrix[i][j];
			}
		}
	}

	public Matrix(double[] vector) {
		row = 1;
		col = vector.length;
		this.matrix = new double[row][col];
		for (int j = 0; j < col; j++) {
			this.matrix[0][j] = vector[j];
		}
	}

	// return C = A + B
	public Matrix plus(Matrix B) {
		final Matrix A = this;
		if (B.row != A.row || B.col != A.col)
			throw new RuntimeException("Illegal matrix dimensions.");
		final Matrix C = new Matrix(row, col);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				C.matrix[i][j] = A.matrix[i][j] + B.matrix[i][j];
			}
		}
		return C;
	}

	// return C = A - B
	public Matrix minus(Matrix B) {
		final Matrix A = this;
		if (B.row != A.row || B.col != A.col)
			throw new RuntimeException("Illegal matrix dimensions.");
		final Matrix C = new Matrix(row, col);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				C.matrix[i][j] = A.matrix[i][j] - B.matrix[i][j];
			}
		}
		return C;
	}

	public double getValue(int row, int col) {
		return matrix[row][col];
	}

	public double[][] getData() {
		return matrix;
	}

	// swap rows i and j
	void swap(int i, int j) {
		final double[] temp = matrix[i];
		matrix[i] = matrix[j];
		matrix[j] = temp;
	}

	// create and return the transpose of the invoking matrix
	public Matrix transpose() {
		final Matrix A = new Matrix(col, row);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				A.matrix[j][i] = this.matrix[i][j];
			}
		}
		return A;
	}

	// does A = B exactly?
	public boolean isEqual(Matrix B) {
		final Matrix A = this;
		if (B.row != A.row || B.col != A.col)
			throw new RuntimeException("Illegal matrix dimensions.");
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (A.matrix[i][j] != B.matrix[i][j])
					return false;
			}
		}
		return true;
	}

	// return C = A * B
	public Matrix times(Matrix B) {
		final Matrix A = this;
		if (A.col != B.row)
			throw new RuntimeException("Illegal matrix dimensions.");
		final Matrix C = new Matrix(A.row, B.col);
		for (int i = 0; i < C.row; i++) {
			for (int j = 0; j < C.col; j++) {
				for (int k = 0; k < A.col; k++) {
					C.matrix[i][j] += A.matrix[i][k] * B.matrix[k][j];
				}
			}
		}
		return C;
	}

	// return x = A^-1 b, assuming A is square and has full rank
	public Matrix solve(Matrix rhs) {
		if (row != col || rhs.row != col || rhs.col != 1)
			throw new RuntimeException("Illegal matrix dimensions.");

		// create copies of the data
		final Matrix A = new Matrix(this);
		final Matrix b = new Matrix(rhs);

		// Gaussian elimination with partial pivoting
		for (int i = 0; i < col; i++) {

			// find pivot row and swap
			int max = i;
			for (int j = i + 1; j < col; j++) {
				if (Math.abs(A.matrix[j][i]) > Math.abs(A.matrix[max][i])) {
					max = j;
				}
			}
			A.swap(i, max);
			b.swap(i, max);

			// singular
			if (A.matrix[i][i] == 0.0)
				throw new RuntimeException("Matrix is singular.");

			// pivot within b
			for (int j = i + 1; j < col; j++) {
				b.matrix[j][0] -= b.matrix[i][0] * A.matrix[j][i] / A.matrix[i][i];
			}

			// pivot within A
			for (int j = i + 1; j < col; j++) {
				final double m = A.matrix[j][i] / A.matrix[i][i];
				for (int k = i + 1; k < col; k++) {
					A.matrix[j][k] -= A.matrix[i][k] * m;
				}
				A.matrix[j][i] = 0.0;
			}
		}

		// back substitution
		final Matrix x = new Matrix(col, 1);
		for (int j = col - 1; j >= 0; j--) {
			double t = 0.0;
			for (int k = j + 1; k < col; k++) {
				t += A.matrix[j][k] * x.matrix[k][0];
			}
			x.matrix[j][0] = (b.matrix[j][0] - t) / A.matrix[j][j];
		}
		return x;

	}

	public void setValue(int row, int col, double value) {
		matrix[row][col] = value;
	}

	public Matrix mult(double alpha) {
		final Matrix A = this;
		final Matrix C = new Matrix(row, col);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				C.matrix[i][j] = A.matrix[i][j] * alpha;
			}
		}
		return C;
	}
}