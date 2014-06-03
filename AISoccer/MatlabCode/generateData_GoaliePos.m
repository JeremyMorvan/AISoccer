function [X,T] = generateData_GoaliePos(nbData)

    X = [];
    T = [];

    load('ANNShootParams');

    for i=1:nbData
        dirb = 100;
        while dirb>pi||dirb<0
            dirb = pi*randn(1)/4+pi/2;
        end
        distb = rand(1)*35+5;
        xb = distb*cos(dirb);
        yb = distb*sin(dirb);

%         dirg = 100;
%         while dirg>pi||dirg<0 
%             dirg = randn(1)*pi/4 + dirb;
%         end
        dirg = rand(1)*pi/2;
        distg = abs(randn(1)*7.01);

        xg = distg*cos(dirg);
        yg = distg*sin(dirg);
        func = @(x) evalNetworkMat(xg,yg,xb,yb,x,W,V);

        xgen = [xg;yg;xb;yb;1];

        X = [X xgen];
        T = [T integral(func,-7.01,7.01)];
    end
    T = T/14.02;
end

