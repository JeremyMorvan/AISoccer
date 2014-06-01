function [X,T] = generateData_GoaliePos

X = [];
T = [];

load('ANNShootParams');

dirb = 100;
while dirb>pi||dirb<0
    dirb = pi*randn(1)/4+pi/2;
end
distb = rand(1)*34+10;
xb = distb*cos(dirb);
yb = dirb*sin(dirb);

dirg = 100;
while dirg>pi||dirg<0 
    dirg = randn(1)*pi/4 + dirb;
end
distg = abs(randn(1)*7.01);

xg = distg*cos(dirg);
yg = dirg*sin(dirg);

func = @(x,y) evalNetwork([xg,yg,xb,yb,x,atan2(y-yb,-xb)],W,V);

xgen = [xg;yg;xb;yb];

X = [X xgen];
T = [T integral2(func,0,3,-7.01,7.01)];

