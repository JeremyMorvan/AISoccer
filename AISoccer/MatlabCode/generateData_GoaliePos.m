function [X,T] = generateData_GoaliePos(nbData)

    X = [];
    T = [];

    load('ANNShootParams');

    for i=1:nbData
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

        func = @(y) evalNetwork([xg;yg;xb;yb;3;atan2(y-yb,-xb);1],W,V);

        xgen = [xg;yg;xb;yb;1];

        X = [X xgen];
        T = [T integral(func,-7.01,7.01)];
    end
end

