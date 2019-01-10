//package com.liaoyin.lyproject.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @项目名：公司内部模板项目
// * @作者：
// * @描述：Swagger配置
// * @日期：Created in 2018/6/8 16:42
// */
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//	/**
//	 * 方法名 createApi
//	 * 创建日期 2017/12/26 15:13
//	 * 创建人
//	 * 描述 创建Docket的Bean。select()函数返回一个ApiSelectorBuilder实例用来控制哪些接口暴露给Swagger来展现，
//	 * 本例采用指定扫描的包路径来定义，Swagger会扫描该包下所有Controller定义的API，并产生文档内容（除了被@ApiIgnore指定的请求）。
//	 */
//	@Bean
//	public Docket createApi() {
//		//表头信息
//		ParameterBuilder clientIdentityPar = new ParameterBuilder();
//		//ClientIdentity_MT表头
//		List<Parameter> pars = new ArrayList<>();
//		clientIdentityPar.name("ClientIdentity_MT").description("客户端标识，值为：_PC、_PCSYSTEM、_Android、_IOS").defaultValue("_PC")
//				.modelRef(new ModelRef("string")).parameterType("header")
//				.required(true).build(); //header中的ClientIdentity_MT必填
//		//FilterToken_MT表头
//		ParameterBuilder filterTokenPar = new ParameterBuilder();
//		filterTokenPar.name("FilterToken_MT").description("令牌")
//				.modelRef(new ModelRef("string")).parameterType("header")
//				.required(false).build(); //header中的filterToken_MT必填
//		pars.add(clientIdentityPar.build());
//		pars.add(filterTokenPar.build());
//		return new Docket(DocumentationType.SWAGGER_2)
//				.globalOperationParameters(pars)
//				.apiInfo(apiInfo())
//				.select()
//				.apis(RequestHandlerSelectors.basePackage("com.liaoyin.lyproject.api"))
//				.paths(PathSelectors.any())
//				.build();
//	}
//
//
//	/**
//	 * 方法名： apiInfo
//	 * 创建日期： 2017/12/26 15:18
//	 * 创建人：
//	 * 描述： 创建该Api的基本信息（这些基本信息会展现在文档页面中）
//	 */
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder()
//				.title("365项目 API")
////				.description("XXX项目 RESTful APIs列表及具体使用说明。<br/>"
////						+ "注意：<br/>"
////						+ "1、在包com.cqgh.statistics.api下所有RESTful APIs会自动生成接口文档（除了被@ApiIgnore指定的APIs）；<br/>"
////						+ "2、@ApiOperation：在方法上标注旨在说明该方法的作用。必填；<br/>"
////						+ "3、@ApiImplicitParams或@ApiImplicitParam:说明传入参数的名称、类型、是否必填。必填；<br/>"
////						+ "4、@ApiModel：在model类上进行注解说明该model的作用；<br/>"
////						+ "5、@ApiModelProperty：对模型中属性添加说明；<br/>"
////						+ "6、@ApiIgnore：应用在Controller范围上，则当前Controller中的所有方法都会被忽略，如果应用在方法上，则对应用的方法忽略暴露API")
//				.termsOfServiceUrl("http://localhost:8051/swagger-ui.html").version("1. 0").build();
//	}
//
//}
