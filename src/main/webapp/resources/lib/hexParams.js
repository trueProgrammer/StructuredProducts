(function () {
    hexParams = function (config) {
        this.defaultParams = config.defaultParams;
        this.optParams = config.optParams;
        this.svg = d3.select('#svg');
        this.hexbin = d3.hexbin().radius(config.radius);
        this.filter = this.svg.append("defs")
            .append("filter")
            .attr("id", "blur")
            .append("feGaussianBlur")
            .attr("stdDeviation", 0);

        var hexAngles = d3.range(Math.PI / 6, 2 * Math.PI + Math.PI / 6, Math.PI / 3);
        var radius = config.radius;

        var self = this;

        window.onAddParamClick = function() {
            self.optParams.splice(0,1);
            prepareDataForLinear(self.optParams);
            self.svg.select('#addParam')
                .selectAll("text")
                .data(self.optParams)
                .exit()
                .remove();
        };
        window.onParamClick = function () {
            self.filter.attr('stdDeviation', 5);
            prepareDataForLinear(self.optParams);
            drawHex(self.optParams, null, 'addParam');
        };

        var prepareDataForLinear = function(params) {
            params[0].x = radius + 10;
            params[0].y = radius + 10;

            for (var i = 1; i < params.length; i++) {
                if (i % 3 === 0) {
                    params[i].x = params[0].x;
                    params[i].y = params[i-1].y + radius*2 + 20;
                } else {
                    params[i].x = params[i-1].x + radius * 2;
                    params[i].y = params[i-1].y;
                }
                params[i].angle = hexAngles[i % hexAngles.length];
            }
        };

        var prepareData = function(params) {
            params[0].x = self.svg[0][0].clientWidth / 2;
            params[0].y = self.svg[0][0].clientHeight / 2;
            params[0].angle = hexAngles[0];

            for (var i = 1; i < params.length; i++) {
                params[i].x = params[0].x + Math.sin(params[i - 1].angle) * radius * 2;
                params[i].y = params[0].y - Math.cos(params[i - 1].angle) * radius * 2;
                params[i].angle = hexAngles[i % hexAngles.length];
            }
        };
        var drawHex = function(params, filter, groupId) {
            //Draw hex
            self.svg.append('g')
                .attr('id', groupId)
                .attr('filter', filter)
                .selectAll('.hexagon')
                .data(params)
                .enter().append("path")
                .attr("class", "hexagon")
                .attr("d", function (d) {
                    return "M" + d.x + "," + d.y + self.hexbin.hexagon();
                })
                .attr('onclick', function (d) {
                    return d.onclick;
                })
                .attr('id', function (d) {
                    return d.id;
                })
                .attr("stroke-width", "5px")
                .attr('stroke', 'white')
                .attr("class","hex-style")
                .transition()
                .attr("stroke", function (d) {
                    return d.stroke
                })
                .duration(1000);

            //Draw texts
            self.svg.selectAll('#' + groupId)
                .selectAll("text")
                .data(params)
                .enter()
                .append("text")
                .attr("x", function (d) {
                    return d.x;
                })
                .attr("y", function (d) {
                    return d.y;
                })
                .attr("class","hex-style")
                .attr("class","hex-text")
                .text(function (d) {
                    return d.text;
                })
                .attr('onclick', function (d) {
                    return d.onclick;
                })
                .transition()
                .duration(1000);

        };
        prepareData(this.defaultParams);
        drawHex(this.defaultParams, 'url(#blur)', 'main');

    }
})();