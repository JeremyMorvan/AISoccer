function [W,V,errors] = delta2(patterns,targets,Win,Vin,eta,epochs,momentum)

W = Win;
V = Vin;
dW = 0;
dV = 0;

errors = zeros(epochs,1);

for i=1:epochs
    Hin = phiFct(W*patterns);
    Hout = [Hin;ones(1,size(patterns,2))];
    Oin = V*Hout;
    Oout = phiFct(Oin);
    errors(i) = sum(sum(abs(sign(Oout) - targets)./2));
    if errors(i)==0
        break;
    end

    delta_o = (Oout - targets) .* ((1 + Oout) .* (1 - Oout)) * 0.5;
    delta_h = (V' * delta_o) .* ((1 + Hout) .* (1 - Hout)) * 0.5;
    delta_h = delta_h(1:(size(delta_h,1)-1), :);

    dW = momentum.*dW-(1-momentum).*delta_h*patterns';
    dV = momentum.*dV-(1-momentum).*delta_o*Hout';
    W = W+eta.*dW;
    V = V+eta.*dV;
end
errors = errors';
end

