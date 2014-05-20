close all;
clear all;
clc;

[X1,X2,T1,T2] = GenerateNsDatas(2,50,50,100,[-1;0],0.1,[1;0],0.1,[0;0],0.1);
[X,T] = shuffle(X1,X2,T1,T2);

hidden = 2;
Win = randn(hidden,3);
Vin = randn(1,hidden+1);

[W,V,errors] = delta2(X,T,Win,Vin,0.1,50,0.9);