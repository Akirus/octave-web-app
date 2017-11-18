Matlab lesson #1

#### Matlab Support in libbingham

Starting with version 0.3.0, all of the core functions in libbingham are supported in Matlab. But, as in the C library, most functions only support Bingham distributions up to dimension 4. (That is, up to S3.)

#### Creating a Bingham Distribution in Matlab

Bingham distributions are represented as a Matlab struct, with fields *d*, *V*, *Z*, and *F* and *dF* (which are computed by libbingham). To create a new Bingham distribution, create a new struct with dimension d, orthogonal direction matrix V, and concentration parameters Z. For example, the uniform Bingham distribution on the 3-D sphere S2 is:
```
B = struct();
B.d = 3;
B.Z = [0,0];
B.V = [0,0; 1,0; 0,1];
```

To look up the normalization constant and its partial derivatives with respect to Z, use:
```
[B.F B.dF] = bingham_F(B.Z);
```

#### Fitting

Given a matrix *X* with unit vectors in the rows, you can compute the maximum likelihood Bingham distribution given *X* with *bingham_fit()*. For example:
```
% create n 4-D unit vectors
n = 10;
X = randn(n,4);
X = X./repmat(sqrt(sum(X.^2,2)), [1,4]);

% Fit a Bingham distribution to X
B = bingham_fit(X);
```

#### Sampling

To sample *n* unit vectors from a Bingham distribution, use:
```
Y = bingham_sample(B,n);
```

#### Computing the PDF

To compute the PDF of a unit vector x under a Bingham B, use:
```
f = bingham_pdf(x,B);
```

#### Computing the Mode
```
mu = bingham_mode(B);
```

#### Computing the Entropy
```
h = bingham_entropy(B);
```

#### Computing the Scatter Matrix

To compute the scatter matrix, $E[\vec{x} \vec{x}^T]$, use:
```
S = bingham_scatter(B);
```

#### Multiplying two Binghams

Two multiply two Binghams, B1 and B2, use:
```
B = bingham_mult(B1,B2);
```

#### Special Functions for the Quaternion Bingham Distribution
```
B2 = bingham_pre_rotate_3d(B,q);
B2 = bingham_post_rotate_3d(q,B);
B2 = bingham_invert_3d(B);
```