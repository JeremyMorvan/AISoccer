close all;
clear all;
clc;

[X1,X2,T1,T2] = ExtractInfos('/bin/TrainingLogs.txt');
[X,T] = shuffle(X1,X2,T1,T2);

hidden = 4;
Win = randn(hidden,7);
Vin = randn(1,hidden+1);

[W,V,errors] = delta2(X,T,Win,Vin,0.1,50,0.9);

save('ANNParams','W','V');

WriteInFile('ANN-Intercepted.txt',W,V);