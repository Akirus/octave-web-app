#### C Support in libbingham / Installation

In bingham/c, run:
```
make
sudo make install
```
The main C header file in libbingham is located at bingham/c/include/bingham.h. Most functions only support Bingham distributions up to dimension 4. (That is, up to S3.)

#### Usage / Examples

After installation, to use the libbingham in your own project, add "-lbingham" to your linker flags include bingham.h at the top of your program. There are several example programs in bingham/c: bingham_sample.c, fit_bingham.c, bingham_lookup.c, cluster_bingham.c, and test_bingham.c.

#### Creating a Bingham Distribution in C

Bingham distributions are represented as a C struct, called *bingham_t*:

```
typedef struct {
  int d;       // dimensions
  double **V;  // axes
  double *Z;   // concentrations
  double F;    // normalization constant
  bingham_stats_t *stats;
} bingham_t;
```

The *bingham_stats_t* struct contains various statistics of the Bingham distribution, such as the entropy, mode, and scatter matrix. (See bingham.h for details.) To create a new *bingham_t*, use one of
```
void bingham_new(bingham_t *B, int d, double **V, double *Z);
void bingham_new_uniform(bingham_t *B, int d);
void bingham_new_S1(bingham_t *B, double *v1, double z1);
void bingham_new_S2(bingham_t *B, double *v1, double *v2, double z1, double z2);
void bingham_new_S3(bingham_t *B, double *v1, double *v2, double *v3, double z1, double z2, double z3);
```

For example,
```
bingham_t B;
bingham_new_uniform(&B, 4);
```

will create a new uniform 4-D Bingham distribution, as will
```
double Z[3] = {0, 0, 0};
double V[3][4] = {{0,1,0,0}, {0,0,1,0}, {0,0,0,1}};
double *Vp[3] = {&V[0][0], &V[1][0], &V[2][0]};
bingham_t B;
bingham_new(&B, 4, Vp, Z);
```

or
```
double v1[4] = {0,1,0,0};
double v2[4] = {0,0,1,0};
double v3[4] = {0,0,0,1};
bingham_t B;
bingham_new(&B, 4, v1, v2, v3, 0, 0, 0);
```

Note that all of the *bingham_new* functions will automatically compute the normalization constant, so there is no need to call *bingham_F(bingham_t *B)* unless you create the bingham_t manually.

**Note:** When you are finished using a *bingham_t*, be sure to free its contents with
```
void bingham_free(bingham_t *B);
```

#### Fitting

Given a matrix X with unit vectors in the rows, you can compute the maximum likelihood Bingham distribution given X with
```
void bingham_fit(bingham_t *B, double **X, int n, int d);
```

where X is an *n-by-d* double matrix created by:
```
double **new_matrix2(int n, int m);
```

which can be found in bingham/c/include/bingham/util.h. For example,
```
// create n 4-D unit vectors
int n = 10;
double **X = new_matrix2(n,4);
for (int i = 0; i < n; i++) {
  for (int j = 0; j < 4; j++)
    X[i][j] = normrand(0,1);
  normalize(X[i], X[i], 4);
}

// fit a Bingham distribution to X
bingham_t B;
bingham_fit(&B, X, n, 4);

// do something with B here...

// cleanup
free_matrix2(X);
bingham_free(&B);
```

**Note:** bingham_fit() allocates memory in the *bingham_t* struct, so make sure the *bingham_t* you pass in is deallocated. Otherwise, calling bingham_fit() will result in a memory leak.

#### Sampling

To sample *n* unit vectors from a Bingham distribution, use:
```
void bingham_sample(double **X, bingham_t *B, int n);
```

For example,
```
bingham_t B;
bingham_new_uniform(&B, 4);
double **X = new_matrix2(100,4);
bingham_sample(X, &B, 100);
```

#### Computing the PDF

To compute the PDF of a unit vector x under a Bingham B, use:
```
double bingham_pdf(double *x, bingham_t *B);
```

For example, assuming bingham_t B exists with B.d = 4,
```
double x[4] = {0,1,0,0};
double f = bingham_pdf(x, &B);
```