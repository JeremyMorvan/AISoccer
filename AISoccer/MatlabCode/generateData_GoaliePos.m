function [X,T] = generateData_GoaliePos(nbData)

    X = [];
    T = [];

    load('ANNShootParams');

    for i=1:nbData
        i
        dirb = 100;
        while dirb>pi||dirb<0
            dirb = pi*randn(1)/4+pi/2;
            dirb
        end
        distb = rand(1)*34+10;
        xb = distb*cos(dirb);
        yb = dirb*sin(dirb);

        dirg = 100;
        while dirg>pi||dirg<0 
            dirg = randn(1)*pi/4 + dirb;
            dirg
        end
        distg = abs(randn(1)*7.01);

        xg = distg*cos(dirg);
        yg = dirg*sin(dirg);

        a=1
        func = @(x,y) evalNetworkMat(xg,yg,xb,yb,x,y,W,V);
        b=1

        xgen = [xg;yg;xb;yb;1];

        X = [X xgen];
        T = [T quad2d(func,0,3,-7.01,7.01)];
        c=1
    end
end

