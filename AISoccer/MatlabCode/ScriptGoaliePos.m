close all;
clear all;
clc;

[X,T] = generateData_GoaliePos(50000);

x = X(1:4,:);

hidden = 5;
Win = 0.1*randn(hidden,5);
Vin = 0.1*randn(1,hidden+1);


[W,V,errors] = delta2(X,T,Win,Vin,1/size(X,2),5000,0.7);

save('ANNGoalPosParams','W','V');

WriteInFile('ANN-Goal.txt',W,V);


