package com.sopromadze.blogapi.serviceTest.todo;

import com.sopromadze.blogapi.exception.BadRequestException;
import com.sopromadze.blogapi.service.impl.ValidatePageNumberAndSizeForTest;
import com.sopromadze.blogapi.utils.AppConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ValidatePageNumberAndSize {

    @InjectMocks
    private ValidatePageNumberAndSizeForTest service;


    //Test: Check validatePageNumberAndSize throws Exception when page or size is negative
    //Input:negative int page, negative int size
    //Output:BadRequestException
    @Test
    @DisplayName("validate page number and size")
    void validatePageNumberAndSize_ThrowsBadRequest (){
        assertThrows(BadRequestException.class,()->service.validatePageNumberAndSize(-1,-2));
    }

    //Test: Check validatePageNumberAndSize when size is greater than AppConstants.MAX_PAGE_SIZE
    //Input:positive int page, AppConstants.MAX_PAGE_SIZE +1
    //Output:BadRequestException
    @Test
    @DisplayName("validate size greater than max allowed")
    void validatePageNumberAndSize_WhenSizeIsGreaterThanMax(){
        assertThrows(BadRequestException.class,()->service.validatePageNumberAndSize(0, AppConstants.MAX_PAGE_SIZE+1));
    }

}
