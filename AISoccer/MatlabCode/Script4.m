close all;
clear all;
clc;

FileNames = {'TrainingLogs.txt','TrainingPassLogs.txt'};
[X1,X2,T1,T2] = ExtractInfos(FileNames);
[X,T] = shuffle(X1,X2,T1,T2);

hidden = 15;
Win = 0.1*randn(hidden,6);
Vin = 0.1*randn(1,hidden+1);


[W,V,errors] = delta2(X,T,Win,Vin,1/size(X,2),10000,0.7);

save('ANNParams','W','V');

WriteInFile('ANN-Intercepted.txt',W,V);