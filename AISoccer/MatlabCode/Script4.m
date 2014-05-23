close all;
clear all;
clc;

[X1,X2,T1,T2] = ExtractInfos('/bin/TrainingLogs.txt');
[X,T] = shuffle(X1,X2,T1,T2);

% hidden = 7;
% Win = 0.1*randn(hidden,6);
% Vin = 0.1*randn(1,hidden+1);
% 
% [W,V,errors] = delta2(X,T,Win,Vin,0.0005,10000,0.7);
% 
% save('ANNParams','W','V');
% 
% WriteInFile('ANN-Intercepted.txt',W,V);