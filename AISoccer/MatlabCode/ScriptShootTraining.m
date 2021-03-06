close all;
clear all;
clc;

FileNames = {'TrainingShootLogs.txt','TrainingShootLogsMe.txt'};
[X1,X2,T1,T2] = ExtractInfosShoot(FileNames);
[X,T] = shuffle(X1,X2,T1,T2);
x = X(1:4,:);

hidden = 15;
Win = 0.1*randn(hidden,5);
Vin = 0.1*randn(1,hidden+1);

[W,V,errors] = delta2(X,T,Win,Vin,1/size(X,2),5000,0.7);

save('ANNShootParams','W','V');

WriteInFile('ANN-Shoot.txt',W,V);