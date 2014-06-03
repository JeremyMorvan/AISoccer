close all;
clear all;
clc;

FileNames = {'TrainingDribbleLogs.txt','TrainingDribbleLogs1.txt'};
[X1,X2,T1,T2] = ExtractInfosDribble(FileNames);
[X,T] = shuffleEqualize(X1,X2,T1,T2);
x = X(1:3,:);

hidden = 5;
Win = 0.1*randn(hidden,4);
Vin = 0.1*randn(1,hidden+1);


[W,V,errors] = delta2(X,T,Win,Vin,1/size(X,2),5000,0.7);

save('ANNDribbleParams','W','V');

WriteInFile('ANN-Dribble.txt',W,V);